package com.cennavi.modules.cross;

import java.io.*;
import java.util.*;

import com.cennavi.utils.GeomtryUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 使用说明：
 * 1.将申请的R（需要申请带名字的）,N,R_LName,R_Name四个文件使用工具转为csv，根据文件名修改下面地址 path、NFilePath、RFilePath、LNameFilePath、NameFilePath
 * 2.执行main方法，得到插入路口表的sql
 * 3.数据库中执行
 */
@Component
public class RoadNetwork2 {

	private static Map<String, Link> linkMap;
	private static Map<String, Node> nodeMap;
	
	// 存储邻接点配对信息，将小ID替换为大ID
	private static Map<String, String> adjoinNodeMap;
	private static Map<String, List<String>> adjoinNodeLinkMap;//邻接点的link放到大的里面，大的nodeid--小的node对应的link

	private static String path = "D:\\data\\cennavi\\test\\lasa\\";
//	private static String path;
//	@Value("${base_data_table}")
//	public void setPath(String tablePath) {
//		try {
//			path = new String(tablePath.getBytes("iso-8859-1"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//
//	}

	/*private static	String NodeFilePath = path+"N.mid";
	private static String LinkFilePathMid = path+"R.mid";
	private static	String LinkFilePathMif = path+"R.mif";*/
	private static String NFilePath = "Nlasa.csv";
	private static String RFilePath = "Rlasa.csv";
	private static String LNameFilePath = "R_LNamelasa.csv";
	private static String NameFilePath = "R_Namelasa.csv";

	private static void init()  {
		if(path==null) {
			//path = "E:\\交警平台\\b项目资料\\泰兴交警\\公司基础数据\\21Q4-02-taixinglevel2-shp\\";
		}
		// 初始化路网：LinkMap+NodeMap
        try {
            long l = System.currentTimeMillis();

            initLinkMap();
            initNodeMap();

            //合并临界点之间的link
			mergeAdjoinNodeLink();

            adjustLinkMap();
            adjustNodeMap();
            System.out.println("初始化路网："+(System.currentTimeMillis()-l));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	//合并邻接点之间的关联的link
	private static void mergeAdjoinNodeLink() {
		for (Map.Entry<String, List<String>> entry : adjoinNodeLinkMap.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			Node node = nodeMap.get(key);
			if(node != null){
//				System.out.println("不为null");
				List<String> linkList = node.getLinkList();
				linkList.addAll(value);
			}

		}
	}

	private static void adjustNodeMap() {
		// 对NodeMap进行调整，有以下几个工作：
		// ① 调整lightFlag，将路口子点也填充lightFlag
		// ② 调整crossLinkList，将路口子点也填充crossLinkList
		// ③ 调整subNodeList，将路口子点也填充subNodeList
		// ④ 在Node拓扑关系中删除不存在的Link
		// ⑤ 建立enterCrossLinkList、leaveCrossLinkList
		// ⑥ 建立enterNodeLinkList、leaveNodeLinkList
		
		Set<String> nodeSet = nodeMap.keySet();
		
		for(String nodeID : nodeSet) {
			Node node = nodeMap.get(nodeID);
			
			String mainNodeID = node.getMainNodeID();
			if(adjoinNodeMap.containsKey(mainNodeID)) {
				mainNodeID = adjoinNodeMap.get(mainNodeID);
			}
			
			if(!mainNodeID.equals("0")) {
				// 若为复杂路口，则：
				// ① 将路口子点填充lightFlag
				fillSubNodeWithLightFlag(node, mainNodeID);
				
				// ② 将路口子点填充crossLinkList
				fillSubNodeWithCrossLinkList(node, mainNodeID);
				
				// ③ 将路口子点填充subNodeList，同时将主点自身加入subNodeList
				fillSubNodeWithSubNodeList(node, mainNodeID);
			}
			
			// ④ 在Node拓扑关系中删除不存在的Link
			// 由于有些Link被一些过滤条件过滤掉，如8级以下道路被删除，在LinkMap中不存在，需要在Node的拓扑关系List中删除掉
			deleteNoExistLink(node);
			
			// ⑤ 建立enterCrossLinkList、leaveCrossLinkList
			buildEnterLeaveCrossLinkList(node);
			
			// ⑥ 建立enterNodeLinkList、leaveNodeLinkList
			buildEnterLeaveNodeLinkList(node);
		}
		
	}

	private static void buildEnterLeaveNodeLinkList(Node node) {
		// 建立enterNodeLinkList、leaveNodeLinkList
		List<String> enterNodeLinkList = new ArrayList<String>();
		List<String> leaveNodeLinkList = new ArrayList<String>();
		/*if(node.getNodeID().equals("20003666942") ) {
			System.out.println(111);
		}*/
		if(node.getNodeID().equals("3663948")) {
			System.out.println(111);
		}
		List<String> linkList = node.getLinkList();
		int linkNum = linkList.size();
		if(linkNum != 0) {
			for(int i = 0; i < linkNum; i++) {
				Link link = linkMap.get(linkList.get(i));
				
				// Link若为流入Link，则加入enterNodeLinkList
				if(isNodeEnterLink(link, node)) {
					enterNodeLinkList.add(link.getLinkID());
				}

				// Link若为流出Link，则加入leaveNodeLinkList
				if(isNodeLeaveLink(link, node)) {
					leaveNodeLinkList.add(link.getLinkID());
				}
			}
		}
		
		node.setEnterNodeLinkList(enterNodeLinkList);
		node.setLeaveNodeLinkList(leaveNodeLinkList);
	}

	private static boolean isNodeLeaveLink(Link link, Node node) {
		// 判断Link是否为流出Node的Link
		String startNode = link.getsNodeID();
				
		if(startNode.equals(node.getNodeID()))
			return true;
				
		return false;
	}

	private static boolean isNodeEnterLink(Link link, Node node) {
		// 判断Link是否为流入Node的Link
		String endNode = link.geteNodeID();
		
		if(endNode.equals(node.getNodeID()))
			return true;
		
		return false;
	}

	private static void buildEnterLeaveCrossLinkList(Node node) {
		// 建立enterCrossLinkList、leaveCrossLinkList
		List<String> enterCrossLinkList = new ArrayList<String>();
		List<String> leaveCrossLinkList = new ArrayList<String>();
		
		List<String> crossLinkList = node.getCrossLinkList();
		int crossLinkNum = crossLinkList.size();
		if(crossLinkNum != 0) {
			for(int i = 0; i < crossLinkNum; i++) {					
				Link crossLink = linkMap.get(crossLinkList.get(i));

				// Link若为流入路口Link，则加入enterCrossLinkList
				if(isCrossEnterLink(crossLink, node)) {
					enterCrossLinkList.add(crossLink.getLinkID());
				}
				
				// Link若为流出路口Link，则加入leaveCrossLinkList
				if(isCrossLeaveLink(crossLink, node)) {
					leaveCrossLinkList.add(crossLink.getLinkID());
				}
			}
		}
		
		node.setEnterCrossLinkList(enterCrossLinkList);
		node.setLeaveCrossLinkList(leaveCrossLinkList);
	}

	private static boolean isCrossLeaveLink(Link crossLink, Node node) {
		// 判断Link是否为流出路口Link
		String startNode = crossLink.getsNodeID();
				
		List<String> subNodeList = node.getSubNodeList();
		boolean isInSubNodeList = subNodeList.contains(startNode);
				
		return isInSubNodeList;
	}

	private static void deleteNoExistLink(Node node) {
		// 在Node拓扑关系中删除不存在的Link
		// 由于有些Link被一些过滤条件过滤掉，如8级以下道路被删除，在LinkMap中不存在，需要在Node的拓扑关系List中删除掉
		// 删除List元素需要使用迭代器方式，不应使用for循环，因为在for循环中删除会导致list大小变化
		List<String> crossLinkList = node.getCrossLinkList();
		Iterator<String> it = crossLinkList.iterator();
		while(it.hasNext()) {
			String crossLinkID = it.next();
			if(!linkMap.containsKey(crossLinkID)) {
				it.remove();
			}
		}
		
		List<String> linkList = node.getLinkList();
		it = linkList.iterator();
		while(it.hasNext()) {
			String linkID = it.next();
			if(!linkMap.containsKey(linkID)) {
				it.remove();
			}
		}
	}

	private static void fillSubNodeWithSubNodeList(Node node, String mainNodeID) {
		// 将路口子点填充subNodeList，同时将主点自身加入subNodeList
		List<String> subNodeList = nodeMap.get(mainNodeID).getSubNodeList();
		
		// 如果subNodeList中不包含主点，则将主点加入subNodeList
		if(!subNodeList.contains(mainNodeID)) {
			subNodeList.add(mainNodeID);
			node.setSubNodeList(subNodeList);
		}
		
		node.setSubNodeList(subNodeList);
	}

	private static void fillSubNodeWithCrossLinkList(Node node, String mainNodeID) {
		// 将路口子点填充crossLinkList
		List<String> mainNodeCrossLinkList = nodeMap.get(mainNodeID).getCrossLinkList();
		node.setCrossLinkList(mainNodeCrossLinkList);
	}

	private static void fillSubNodeWithLightFlag(Node node, String mainNodeID) {
		// 将路口子点填充lightFlag
		int mainNodeLightFlag = nodeMap.get(mainNodeID).getLightFlag();
		node.setLightFlag(mainNodeLightFlag);
	}

	private static boolean isCrossEnterLink(Link crossLink, Node node) {
		// 判断Link是否为流入路口Link
		String endNode = crossLink.geteNodeID();
		
		List<String> subNodeList = node.getSubNodeList();
		boolean isInSubNodeList = subNodeList.contains(endNode);
		
		return isInSubNodeList;
	}

	private static void adjustLinkMap() {
		// 对LinkMap进行调整，有以下几个工作：
		// 若Link的起终点NodeID为邻接点，则将需替换的邻接点替换为大ID的Node
		Set<String> linkSet = linkMap.keySet();
		
		for(String linkID : linkSet) {
			Link link = linkMap.get(linkID);
			
			String startNode = link.getsNodeID();
			if(adjoinNodeMap.containsKey(startNode)) {
				link.setsNodeID(adjoinNodeMap.get(startNode));
			}
			
			String endNode = link.geteNodeID();
			if(adjoinNodeMap.containsKey(endNode)) {
				link.seteNodeID(adjoinNodeMap.get(endNode));
			}
		}
	}

	private static void initNodeMap() throws FileNotFoundException, IOException {
		// 初始化Node集合
		nodeMap = new HashMap<>();
		adjoinNodeMap = new HashMap<>();
		adjoinNodeLinkMap = new HashMap<>();

//		String nodeFilePath = RoadNetworkConfig.getNodeFilePath();
		String nodeFilePath = path + NFilePath;

		BufferedReader br = new BufferedReader(new FileReader(nodeFilePath));
		
		String temp = null;
		while((temp = br.readLine()) != null) {
			// 构造Node对象
			if(temp.contains("WKT")) {
				continue;
			}
			addNode(temp);
		}
		
		br.close();
	}

	private static void addNode(String temp) {
		if(temp.contains("MapID")){
			return;
		}
		// 构造Node对象
		//String[] splits = temp.replace("\"","").split(",");
//		String[] ss = temp.split("\\)\",");//处理经纬度
		String[] ss  = temp.split(";");

//		String[] splits = ss[1].replace("\"","").split(",");
		String[] splits = ss[0].split(",");

//		String geom = "point("+ss[0].split("\\(")[1]+")";
		String geom = ss[1].replace("srid=4326;","");
		String meshID = splits[0];
		String nodeID = splits[1];

		int crossFlag = Integer.valueOf(splits[4]);
		int lightFlag = Integer.valueOf(splits[5]);
		
		List<String> crossLinkList = new ArrayList<String>();
		if(!splits[6].equals("0")) {
			String[] crossLinkSplits = splits[6].split("\\|");
			int crossLinkNum = crossLinkSplits.length;
			for(int i = 0; i < crossLinkNum; i++) {
				crossLinkList.add(crossLinkSplits[i]);
			}
		}
		
		String mainNodeID = splits[7];
		
		List<String> subNodeList = new ArrayList<String>();
		if(!splits[8].equals("0")) {
			String[] subNode1Splits = splits[8].split("\\|");
			int subNode1Num = subNode1Splits.length;
			for(int i = 0; i < subNode1Num; i++) {
				subNodeList.add(subNode1Splits[i]);
			}
		}
		if(!splits[9].equals("") && !splits[9].equals("0")) {
			String[] subNode2Splits = splits[9].split("\\|");
			int subNode2Num = subNode2Splits.length;
			for(int i = 0; i < subNode2Num; i++) {
				subNodeList.add(subNode2Splits[i]);
			}
		}
		
		String adjoinNodeID = splits[11];

		List<String> linkList = new ArrayList<String>();
		String[] linkSplits = splits[12].split("\\|");
		int linkNum = linkSplits.length;
		for(int i = 0; i < linkNum; i++) {
			// 如果link为双向通行，在node的linkList中需要加入该link的反向Link
			String targetLinkID = linkSplits[i];
			linkList.add(targetLinkID);
			
			String reverseLinkID = "-" + targetLinkID;
			if(linkMap.containsKey(reverseLinkID)) {
				linkList.add(reverseLinkID);
			}
		}

		// 若存在邻接点ID且邻接点ID大于NodeID，则只保存大ID的Node，即本NodeID不保存
		if(judgeAdjoinNode(nodeID, adjoinNodeID)) {
			adjoinNodeLinkMap.put(adjoinNodeID,linkList);
			return;
		}

		Node node = new Node(meshID, nodeID, crossFlag, lightFlag, geom,crossLinkList, mainNodeID, subNodeList, linkList);
		nodeMap.put(nodeID, node);
	}

	private static boolean judgeAdjoinNode(String nodeID, String adjoinNodeID) {
		// 若存在邻接点ID且邻接点ID大于NodeID，则只保存大ID的Node，即本NodeID不保存
		long LNodeID = Long.valueOf(nodeID);
		long LAdjoinNodeID = Long.valueOf(adjoinNodeID);
		
		if((LAdjoinNodeID != 0) && (LNodeID < LAdjoinNodeID)) {
			adjoinNodeMap.put(nodeID, adjoinNodeID);
			return true;
		}
		return false;
	}

	private static void initLinkMap() throws IOException {
		// 初始化Link集合
		// 首先读入Mif文件，初始化形状点序列集合
		//List<List<Point>> linkShapeList = new ArrayList<List<Point>>();
		//getLinkShapeList(linkShapeList);

        // 读入LName与Name表，初始化道路名称Map
		Map<String, String> nameMap = new HashMap<>();
		getLinkNameMap(nameMap);
		
		linkMap = new HashMap<>();

		String linkFilePath = path + RFilePath;
		BufferedReader br = new BufferedReader(new FileReader(linkFilePath));
		String temp = null;
		while((temp = br.readLine()) != null) {
			if(temp.contains("WKT")) {
				continue;
			}
			// 构造Link对象
			addLink(temp, nameMap);
		}
		
		br.close();
	}

	private static void getLinkNameMap(Map<String, String> nameMap) throws IOException {
		// 读入LName与Name表，初始化道路名称Map
		//String LNamePath = RoadNetworkConfig.getLNameFilePath();
		//String NamePath = RoadNetworkConfig.getNameFilePath();
		String LNamePath = path + LNameFilePath;
		String NamePath = path + NameFilePath;

		Map<String, String> link_nameID_Map = new HashMap<String, String>(); // LinkID--NameID
		buildLink_NameID_Map(link_nameID_Map, LNamePath);

		Map<String, String> nameID_NameMap = new HashMap<String, String>(); // NameID--Name
		buildNameID_Name_Map(nameID_NameMap, NamePath);

		Set<String> keySet = link_nameID_Map.keySet();
		for(String linkID :keySet) {
			String nameID = link_nameID_Map.get(linkID);
			if(nameID_NameMap.containsKey(nameID)) {
				String name = nameID_NameMap.get(nameID);
				nameMap.put(linkID, name);
//				System.out.println(linkID + ": " + name);
			}
		}
	}

	private static void buildNameID_Name_Map(Map<String, String> nameID_NameMap, String namePath) throws IOException {
		// 构建对应关系Map：NameID--Name
		// 读取CSV中文乱码，强制指定输入流编码为GBK
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(namePath), "utf8"));
		
		int num = 0; // 由于奇数行为汉语名称，双数行为英语名称，所以用该变量计数
		String temp = null;
		while((temp = br.readLine()) != null) {
			if(temp.contains("Route_ID")) continue;
			String[] splits = temp.replace("\"","").split(",");
			if(num % 2 == 0) { // 奇数行为汉语名称
				String nameID = splits[0];
				String name = splits[2];
				nameID_NameMap.put(nameID, name);
			}
			
			num++;
		}
		
		br.close();
	}

	private static void buildLink_NameID_Map(Map<String, String> link_nameID_Map, String lNamePath) throws IOException {
		// 构建对应关系Map：Link--NameID
		BufferedReader br = new BufferedReader(new FileReader(lNamePath));
		
		String temp = null;
		while((temp = br.readLine()) != null) {
			if(temp.contains("MapID")) continue;
			String[] splits = temp.replace("\"","").split(",");
			
			String linkID = splits[1];
			String nameID = splits[2];
			
			link_nameID_Map.put(linkID, nameID);
		}
		
		br.close();
	}

	private static void getLinkShapeList(List<List<Point>> linkShapeList) throws FileNotFoundException, IOException {
		// 读入Mif文件，初始化形状点序列集合
//		String mifPath = RoadNetworkConfig.getMifFilePath();
		String mifPath = null;

		BufferedReader br = new BufferedReader(new FileReader(mifPath));
		
		String temp = null;
		while((temp = br.readLine()) != null) {
			String[] splits = temp.split(" ");
			if(splits[0].equals("Line")) {
				double lon1 = Double.valueOf(splits[1]);
				double lat1 = Double.valueOf(splits[2]);
				double lon2 = Double.valueOf(splits[3]);
				double lat2 = Double.valueOf(splits[4]);
				
				Point point1 = new Point(lon1, lat1);
				Point point2 = new Point(lon2, lat2);
				List<Point> shortPointList = new ArrayList<Point>();
				shortPointList.add(point1);
				shortPointList.add(point2);
				
				linkShapeList.add(shortPointList);
			}
			else if(splits[0].equals("Pline")) {
				int pointNum = Integer.valueOf(splits[1]);
				List<Point> longPointList = new ArrayList<Point>();
				
				for(int i = 0; i < pointNum; i++) {
					String tempPoint = br.readLine();
					String[] pointSplits = tempPoint.split(" ");
					
					double lon = Double.valueOf(pointSplits[0]);
					double lat = Double.valueOf(pointSplits[1]);
					Point point = new Point(lon, lat);
					
					longPointList.add(point);
				}
				
				linkShapeList.add(longPointList);
			}
		}
		
		br.close();

	}

	private static void addLink(String temp,Map<String, String> nameMap) {
		if(temp.contains("MapID")){//说明是第一行数据，不需要解析
			return;
		}
		// 构造Link对象
//		String[] ss = temp.split("\\)\",");//处理经纬度
		String[] ss  = temp.split("\\\"");
//		String[] splits = ss[1].replace("\"","").split(",");
		String[] splits = ss[0].split(",");

//		String geom = "LINESTRING("+ss[0].split("\\(\\(")[1];
		String geom = ss[1].replace("srid=4326;","");
		String meshID = splits[0];
		String linkID = splits[1];
		if(linkID.equals("17064325")) {
			//System.out.println(111);
		}

		String kindClasses = splits[3];
		String[] kindClassSplits = kindClasses.split("\\|");
		
		String SLinkKind = kindClassSplits[0].substring(0, 2);
		// 删掉8级以下道路，包括09：非机动车通行道路；0a：航线、轮渡；0b：行人道路
		if(SLinkKind.equals("09") || SLinkKind.equals("0a") || SLinkKind.equals("0b") || SLinkKind.equals("0c")) {
			return;
		}
		
		int linkKind = Integer.valueOf(SLinkKind);
		List<String> linkClass = new ArrayList<>();
		int kindNum = kindClassSplits.length;
		for(int i = 0; i < kindNum; i++) {
			String sClass = kindClassSplits[i].substring(2, 4);
			linkClass.add(sClass);
		}
		
		int width = Integer.valueOf(splits[4]);
		int direction = Integer.valueOf(splits[5]);

		String sNodeID = splits[9];
		String eNodeID = splits[10];

		// FRC
		int FRC = Integer.valueOf(splits[11]);
		
		// Link长度  单位：米
		int length = (int)(Double.valueOf(splits[12]) * 1000);
		
		// 原始地图数据中起点至终点方向车道数（若画线方向为逆方向则需反转）
		int originS2ELaneNum = -1;
		if(!splits[25].equals("")) {
			originS2ELaneNum = Integer.valueOf(splits[25]);
		}
		// 原始地图数据中终点至起点方向车道数（若画线方向为逆方向则需反转）
		int originE2SLaneNum = -1;
		if(!splits[26].equals("")) {
			originE2SLaneNum = Integer.valueOf(splits[26]);
		}
		
		double originS2ESpeedLimit = -1;
		if(!splits[33].equals("")) {
			originS2ESpeedLimit = Double.valueOf(splits[33]);
		}
		double originE2SSpeedLimit = -1;
		if(!splits[34].equals("")) {
			originE2SSpeedLimit = Double.valueOf(splits[34]);
		}
		
		// 计算Link的空间四至
		double maxLon = 0;
		double minLon = Double.MAX_VALUE;
		double maxLat = 0;
		double minLat = Double.MAX_VALUE;


		// 得到Link的道路名称
		String name = "未知道路";
		if(nameMap.containsKey(linkID)) {
			name = nameMap.get(linkID);
		}
		if(splits.length>=44 && StringUtils.isNotBlank(splits[44])) {
			name = splits[44];
		}

		// 基于Link方向构造Link加入Map
		addLink2MapByDir(geom, meshID, linkID, linkKind, linkClass, direction, sNodeID, eNodeID, FRC, length, originS2ELaneNum, originE2SLaneNum, originS2ESpeedLimit, originE2SSpeedLimit, maxLon, minLon, maxLat, minLat, name, width);
	}

	private static void addLink2MapByDir(String geom, String meshID, String linkID, int linkKind, List<String> linkClass,
										 int direction, String sNodeID, String eNodeID, int FRC, int length, int originS2ELaneNum, int originE2SLaneNum,
										 double originS2ESpeedLimit, double originE2SSpeedLimit, double maxLon, double minLon, double maxLat, double minLat, String name, int width) {
		// 基于Link方向构造Link加入Map
		if(direction == 0 || direction == 1) {
			// 双向通行需构造两条Link
			Link link = new Link(meshID, linkID, linkKind, linkClass, sNodeID, eNodeID, FRC, length,
					originS2ELaneNum, originS2ESpeedLimit, geom, maxLon, minLon, maxLat, minLat, name, width);
			//String geompy = JtsTools.thickLineStripeCalculate(geom).toString();//偏移经纬度
			String geompy = JtsTools.CalculatePolygonByWktLine1(geom).toString();//偏移经纬度
			link.setGeompy(geompy);
			linkMap.put(linkID, link);

			// 双向通行的反向Link其ID前加负号
			String reverseLinkID = "-" + linkID;
			// 反向Link方向与画线方向相反，因此其形状点序列要反转
			//List<Point> reverseShapePointList = reverseList(shapePointList);
			String reverseStr = reverseList(geom);
			Link reverseLink = new Link(meshID, reverseLinkID, linkKind, linkClass, eNodeID, sNodeID, FRC, length, originE2SLaneNum, originE2SSpeedLimit, reverseStr, maxLon, minLon, maxLat, minLat, name, width);
			//String reverseStrPy = JtsTools.thickLineStripeCalculate(reverseStr).toString();//偏移经纬度
			String reverseStrPy = JtsTools.CalculatePolygonByWktLine1(reverseStr).toString();//偏移经纬度
			reverseLink.setGeompy(reverseStrPy);
			linkMap.put(reverseLinkID, reverseLink);
		} else if(direction == 2) {
			Link link = new Link(meshID, linkID, linkKind, linkClass, sNodeID, eNodeID, FRC, length, originS2ELaneNum, originS2ESpeedLimit, geom, maxLon, minLon, maxLat, minLat, name, width);
			String geompy = JtsTools.CalculatePolygonByWktLine(geom).toString();//双侧扩充成面
			link.setGeompy(geompy);
			linkMap.put(linkID, link);
		} else if(direction == 3) {
			// 方向值为3，其Link方向与画线方向相反，因此其形状点序列要反转
			//List<Point> reverseShapePointList = reverseList(shapePointList);
			String reverseStr = reverseList(geom);
			Link link = new Link(meshID, linkID, linkKind, linkClass, eNodeID, sNodeID, FRC, length, originE2SLaneNum, originE2SSpeedLimit, reverseStr, maxLon, minLon, maxLat, minLat, name, width);
			//link.setGeompy(reverseStr);
			String geompy = JtsTools.CalculatePolygonByWktLine(reverseStr).toString();//双侧扩充成面
			link.setGeompy(geompy);
			linkMap.put(linkID, link);
		}
	}

	private static List<Point> reverseList(List<Point> shapePointList) {
		// 将List进行反向
		List<Point> reverseShapePointList = new ArrayList<Point>();
		
		int shapePointNum = shapePointList.size();
		for(int i = 0; i < shapePointNum; i++) {
			reverseShapePointList.add(shapePointList.get(shapePointNum - i - 1));
		}
		return reverseShapePointList;
	}

	private static String reverseList(String wkt) {
		// 将List进行反向
		List<String> reverseList = new ArrayList<>();
		String str = wkt.split("\\(")[1].replace(")", "");
		String[] ss = str.split(",");
		int num = ss.length;
		for(int i = 0; i < num; i++) {
			String s = ss[num - i - 1];
			reverseList.add(s);
		}
		String result = "LINESTRING("+StringUtils.join(reverseList,",")+")";
		return result;
	}

	@PostConstruct
	public static Map<String, Link> getLinkMap()  {
		if(linkMap == null) {
			init();
		}
		return linkMap;
	}
	
	public static void setLinkMap(Map<String, Link> linkMap) {
		RoadNetwork2.linkMap = linkMap;
	}
	
	public static Map<String, Node> getNodeMap() {
		if(nodeMap == null) {
			init();
		}
		return nodeMap;
	}
	
	public static void setNodeMap(Map<String, Node> nodeMap) {
		RoadNetwork2.nodeMap = nodeMap;
	}

	private static void makeJunction(){
		Map<String, Link> linkMap = RoadNetwork2.getLinkMap();
		Map<String, Node> nodeMap = RoadNetwork2.getNodeMap();
		Iterator iter = nodeMap.entrySet().iterator();
		int num = 0;
		while (iter.hasNext()) {
			Map.Entry<String,Node> entry = (Map.Entry<String,Node>) iter.next();
			Node node = entry.getValue();
			//2单一路口，3复合路口的主点
			int crossFlag = node.getCrossFlag();
			System.out.println("是不是路口:"+crossFlag);
			if(crossFlag==2 || crossFlag==3) {
				String nodeID = node.getNodeID();
				List<String> linkList = node.getCrossLinkList();
				Set<String> set = new HashSet<>();
				for (int i = 0; i < linkList.size(); i++) {
					String name = linkMap.get(linkList.get(i)).getName();
					if(name.equals("未知道路")) {continue;}
					set.add(name);
				}
				List<String> list_1 = new ArrayList<>(set);
				String join = StringUtils.join(list_1, "-");
				if(list_1.size()>=2) {
					String format = "3212839"+String.format("%05d", num);
					System.out.println("insert into junction_info values('"+format+"','"+join+"',st_geomfromtext('"+node.getGeom()+"'));");
					num++;
				}
			}
		}
	}
	// Debug
	public static void main(String[] args) throws IOException {
		makeJunction();
//		Map<String, String> adjacentJunction = getAdjacentJunction("86704997");
//		Map<String, String> adjacentJunction = getAdjacentJunction("509010261");
//		Map<String, String> adjacentJunction = getAdjacentJunction("5180991");//出现重合线路，先执行后右转
		//Map<String, String> adjacentJunction = getAdjacentJunction("17024144");//只有直行
//		Map<String, String> adjacentJunction = getAdjacentJunction("601010292");//只有直行
//		Map<String, String> adjacentJunction = getAdjacentJunction("81744981");//没有左转
//		Map<String, String> adjacentJunction = getAdjacentJunction("505116785");//右转路径规划错误，没有左转
//		Map<String, Node> adjacentJunction = getAdjacentJunction("-606011233");//只有右转
		//Map<String, Node> adjacentJunction = getAdjacentJunction("5187156");//北向南，
		//System.out.println("11"+adjacentJunction.size());

	}

	/**
	 * 获取nodeid所在的路口的邻接路口
	 * @param linkid 车辆所在link, 需要使用起终点nodeid
	 * @return 1.直行，2左转，3右转
	 */
	public static Map<String, Node> getAdjacentJunction(String linkid) {
		Map<String, Link> linkMap = RoadNetwork2.getLinkMap();
		Map<String, Node> nodeMap = RoadNetwork2.getNodeMap();
		Node aaaa = nodeMap.get("10003666942");
		Node a1= nodeMap.get("20003666942");
		Link link1 = linkMap.get("5182452");

		Link startLink = linkMap.get(linkid);
		Node snode = nodeMap.get(startLink.getsNodeID());
		Node node = nodeMap.get(startLink.geteNodeID());
		double azimuth = MapUtil.azimuth(snode.getGeom(), node.getGeom());
		List<String> leaveNodeLinkList = node.getLeaveNodeLinkList();
		//当node不是路口内的点，需要向下找到是路口的node;//todo 此处在制作设备和link关系，尽可能准确，可避免以下判断，此处没有排除反方向的路口
		//while(node.getCrossFlag()==0 || leaveNodeLinkList.size()<=2) {
		boolean whileBool = true;
		while(node.getCrossFlag()==0 && whileBool) {//不是路口继续找路口
			List<String> leaveNodeLinkList1 = leaveNodeLinkList;
			for (String linkid1: leaveNodeLinkList1) {
				if(!linkid.contains("-")) {
					String enode = linkMap.get(linkid1).geteNodeID();
					node = nodeMap.get(enode);
					leaveNodeLinkList = node.getLeaveNodeLinkList();
					//if(node.getCrossFlag()> 0 || node.getLeaveNodeLinkList().size()>2) {
					if(node.getCrossFlag()> 0) {
						whileBool = false;
						break;
					}
				}
			}
		}
		//针对虽然是路口，但该路口是主路和辅路的路口，判断leaveNodeLinkList的有效link是否大于等于2，否，则用下一个node点
		for (int i = 0; i < leaveNodeLinkList.size(); i++) {
			String linkid0 = leaveNodeLinkList.get(i);
			String linkid01 = linkid0.replace("-", "");
			String linkid02 = linkid.replace("-", "");
			if(linkid01.equals(linkid02)) {
				leaveNodeLinkList.remove(linkid0);
			}
		}
		if(leaveNodeLinkList.size()==1) {
			String enode = linkMap.get(leaveNodeLinkList.get(0)).geteNodeID();
			node = nodeMap.get(enode);
			leaveNodeLinkList = node.getLeaveNodeLinkList();
		}

		//针对设备所在位置在右转道前，会将直行和右转的node计算成此设备的路口，判断直行的下一个node是否是node
		//增加规则：两个路口不是主辅节点
		if(node.getCrossFlag()==2) {//排除复合路口
			for (int j = 0; j < leaveNodeLinkList.size(); j++) {
				Link link2 = linkMap.get(leaveNodeLinkList.get(j));
				Node node2 = nodeMap.get(link2.geteNodeID());
				double azimuth2 = MapUtil.azimuth(node.getGeom(),node2.getGeom());
				double abs1 = Math.abs(azimuth - azimuth2);
				if(abs1<20) {//直行link
					if(node2.getCrossFlag() > 0) {
						node = node2;
						leaveNodeLinkList = node.getLeaveNodeLinkList();
					}
				}
			}
		}

		int startCrossFlag = node.getCrossFlag();
		Map<String, Node> map = new HashMap<>();//1.直行，2左转，3右转
		for (int i = 0; i < leaveNodeLinkList.size(); i++) {
			String linkid1 = leaveNodeLinkList.get(i);
			Link link = linkMap.get(linkid1);
			Node curr = nodeMap.get(link.geteNodeID());

			//通过路口标识crossFlag判断左转直行右转，当路口是复杂路口时使用
			//普通路口使用方位角
			//原始数据中crossFlag有误，改为通过方位角计算的偏差计算,方位角之差需要45为直行，大约45且小于原始方位是左转，否则是右转，之差接近180则为掉头
			//部分复合路口，但和普通路口一致
			//判断复合路口，垂直方向是双行道的情况（直行道路的下一个点非路口）-按照单一路口计算
			//北向南的方位角要单独判断
			if(startCrossFlag==2) {
				method2(curr, node, azimuth,map);
			}else {
				/*//判断复合路口，垂直方向是双行道的情况（直行道路的下一个点非路口）-按照单一路口计算
				if( ) {
					method2(curr, node, azimuth,map);
				}*/
				method1(curr,node, azimuth,map);
			}
		}
		map.put("rightCorrect",node);//增加右转修正点，处理有专门的右转车道的路口
		return map;
	}

	//判断是直行、左转、右转 //1.直行，2左转，3右转
	private static void method2(Node curr, Node startnode,double azimuth, Map<String,Node> map) {
		double azimuth1 = MapUtil.azimuth(startnode.getGeom(),curr.getGeom());
		if(azimuth>135 && azimuth1<0) {//北向南
			azimuth1 += 360;
		}
		double abs = Math.abs(azimuth - azimuth1);
		if(abs<160) {//排除掉头
			Node nextNode = getNextNode(curr, startnode, startnode, nodeMap, linkMap);
			if(nextNode!=null) {
				if(abs<20) {//直行
					//map.put("1", nextNode.getGeom());
					map.put("1", nextNode);
				} else if(azimuth > azimuth1) {//左转
					map.put("2", nextNode);
				} else if (azimuth < azimuth1) {//右转
					map.put("3", nextNode);
				}
			}
		}
	}

	//复杂路口，判断是直行、左转、右转
	private static void method1(Node curr, Node startnode, double azimuth, Map<String,Node> map) {
		double azimuth1 = MapUtil.azimuth(startnode.getGeom(),curr.getGeom());
		if(azimuth>135 && azimuth1<0) {//北向南
			azimuth1 += 360;
		}
		double abs = Math.abs(azimuth - azimuth1);
		if(abs<160) {//排除掉头
			if(abs<20) {//直行
				Node nextNode = getNextNode(curr, startnode, startnode, nodeMap, linkMap);
				if(nextNode!=null) {
					map.put("1", nextNode);
				}

				List<String> leaveNodeLinkList2 = curr.getLeaveNodeLinkList();
				for (int j = 0; j < leaveNodeLinkList2.size(); j++) {
					Link link2 = linkMap.get(leaveNodeLinkList2.get(j));
					Node node2 = nodeMap.get(link2.geteNodeID());
					double azimuth2 = MapUtil.azimuth(startnode.getGeom(),node2.getGeom());
					double abs1 = Math.abs(azimuth1 - azimuth2);
					if(abs1>20 && azimuth1 > azimuth2) {//左转
						Node nextNode1 = getNextNode(node2, curr,startnode, nodeMap, linkMap);
						if(nextNode1!=null) {
							map.put("2", nextNode1);
						}
					}
				}

			} else if(azimuth > azimuth1) {//左转或者掉头
				//
			} else if (azimuth < azimuth1) {//右转
				Node nextNode = getNextNode(curr, startnode,startnode, nodeMap, linkMap);
				if(nextNode!=null) {
					map.put("3", nextNode);
				}
			}
		}
		//原始数据中crossFlag有误，改为通过方位角计算的偏差计算
		/*int crossFlag = curr.getCrossFlag();
		String mainNodeID = curr.getMainNodeID();
		if(crossFlag==0) {//非路口，右转
			Node nextNode = getNextNode(curr, node, "",node, nodeMap, linkMap);
			if(nextNode!=null) {
				map.put("3", nextNode.getGeom());
			}
		} else {//直行，需要区分出左转
			List<String> leaveNodeLinkList2 = curr.getLeaveNodeLinkList();
			String excludeNodeId = "";
			for (int j = 0; j < leaveNodeLinkList2.size(); j++) {
				String linkid2 = leaveNodeLinkList2.get(j);
				String s2 = linkMap.get(linkid2).geteNodeID();
				Node node2 = nodeMap.get(s2);
				int crossFlag2 = node2.getCrossFlag();
				if(crossFlag2>0 && node2.getMainNodeID().equals(mainNodeID)) {//是路口，相同的主点,则为左转
					Node nextNode = getNextNode(nodeMap.get(s2), curr, "",node, nodeMap, linkMap);
					if(nextNode!=null) {
						map.put("2", nextNode.getGeom());
					}
					excludeNodeId = s2;
				}
			}
			Node nextNode = getNextNode(curr, node, excludeNodeId, node, nodeMap, linkMap);
			if(nextNode!=null) {
				map.put("1", nextNode.getGeom());
			}
		}*/
	}



	/**
	 * 	//获取是路口的
	 * @param node 本次node
	 * @param lastNode 上一个node
	 * //@param azimuth 此段路线的方位角
	 * @param startNode 最开始的node
	 * @param nodeMap
	 * @param linkMap
	 * @return
	 */
	private static Node getNextNode(Node node,Node lastNode, Node startNode, Map<String, Node> nodeMap, Map<String, Link> linkMap) {
		if(node==null) {//当一个Node没有下一个，或者只有反方向的link
			return null;
		}
		//判断是否为路口, 是路口、且和起点不是一组（即主节点不一致）、且距离起点要大于100米
		List<String> leaveNodeLinkList = node.getLeaveNodeLinkList();
		boolean isCrossBool = node.getCrossFlag()>0;//是否是路口
		//boolean isCrossBool = node.getCrossFlag()>0 || leaveNodeLinkList.size()>2;//是否是路口
		boolean isSamCrossBool = !node.getMainNodeID().equals("0") && node.getMainNodeID().equals(lastNode.getMainNodeID());//是否是相同的路口
		if(isCrossBool && !isSamCrossBool) {
			double distance = GeomtryUtils.getDistanceForWkt(startNode.getGeom(), node.getGeom());
			if(distance>100) {
				return node;
			}
		}
		//不是满足要去的路口，则继续向下找
		Node cNode = null;
		double minAngle = 99;
		for (int i = 0; i < leaveNodeLinkList.size(); i++) {
			String linkid = leaveNodeLinkList.get(i);
			Link link = linkMap.get(linkid);
			String s = link.geteNodeID();

			Node nextNode = nodeMap.get(s);

			double azimuth = MapUtil.azimuth(lastNode.getGeom(),node.getGeom());
			double azimuth1 = MapUtil.azimuth(node.getGeom(),nextNode.getGeom());
			double abs = Math.abs(azimuth - azimuth1);
			if(abs<20) {//保留直行
				return getNextNode(nextNode, node,startNode, nodeMap, linkMap);
			}
			//夹角有问题，改为方位角
			/*String p1 = lastNode.getGeom().replace(")", "").split("\\(")[1];
			String p2 = node.getGeom().replace(")", "").split("\\(")[1];
			String p3 = nextNode.getGeom().replace(")", "").split("\\(")[1];
			double angle = getDegree(p1, p2, p3);
			if(minAngle>=angle) {//取角度最小的
				minAngle = angle;
				cNode = nextNode;
			}*/
		}
		//return getNextNode(cNode, node, azimuth,startNode, nodeMap, linkMap);
		return null;
	}

	//p1和p3是两个顶点
	private static double getDegree(String p1, String p2, String p3) {
		String[] ps1 = p1.split(" ");
		String[] ps2 = p2.split(" ");
		String[] ps3 = p3.split(" ");
		double x1=Double.parseDouble(ps1[0]);
		double y1=Double.parseDouble(ps1[1]);
		double x2=Double.parseDouble(ps2[0]);
		double y2=Double.parseDouble(ps2[1]);
		double x3=Double.parseDouble(ps3[0]);
		double y3=Double.parseDouble(ps3[1]);

		return getDegree(x1,y1,x2,y2,x3,y3);
	}

	//ABC,获取CAB的角度
	private static double getDegree(double x0, double y0,double x1, double y1,double x2, double y2) {
		//向量的点乘
		double vector = (x1 - x0)*(x2 - x0) + (y1 - y0)*(y2 - y0);
		//向量的模乘
		double sqrt = Math.sqrt(
				(Math.abs((x1-x0)*(x1-x0)) + Math.abs((y1-y0)*(y1-y0))) *
						(Math.abs((x2-x0)*(x2-x0)) + Math.abs((y2-y0)*(y2-y0)))
		);
		//反余弦计算弧度
		double radian = Math.acos(vector/sqrt);
		//弧度转及角度
		return 180* radian / Math.PI;
	}

	private static double getCos(String p1, String p2, String p3) {
		String[] ps1 = p1.split(" ");
		String[] ps2 = p2.split(" ");
		String[] ps3 = p3.split(" ");
		double x1=Double.parseDouble(ps1[0]);
		double y1=Double.parseDouble(ps1[1]);
		double x2=Double.parseDouble(ps2[0]);
		double y2=Double.parseDouble(ps2[1]);
		double x3=Double.parseDouble(ps3[0]);
		double y3=Double.parseDouble(ps3[1]);
		Double d1=Math.pow((Math.pow((x2-x1),2)+Math.pow((y2-y1),2)),0.5);
		Double d2=Math.pow((Math.pow((x3-x2),2)+Math.pow((y3-y2),2)),0.5);
		Double d3=Math.pow((Math.pow((x3-x1),2)+Math.pow((y3-y1),2)),0.5);

		return (d1*d1 + d3*d3 - d2*d2 ) / 2*d1*d3;
	}
}
