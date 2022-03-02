package com.cennavi.modules.cross;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private String meshID;
	private String nodeID;
	private int crossFlag; // 路口标识
	private int lightFlag; // 红绿灯标识 0：无红绿灯 1：有红绿灯，无论Node是路口主点还是子点，此字段均有数据

    private String geom;
	// 可为空集
	private List<String> crossLinkList; // 路口接续Link List，无论Node是路口主点还是子点，此字段均有数据
	
	private String mainNodeID; //路口主点ID
	
	// 可为空集
	private List<String> subNodeList; //路口子点集合，无论Node是路口主点还是子点，此字段均有数据
	
	private List<String> linkList;
	
	// 以下字段需从上面的字段中生成
	private List<String> enterCrossLinkList;
	private List<String> leaveCrossLinkList;
	private List<String> enterNodeLinkList;
	private List<String> leaveNodeLinkList;
	
	public Node(String meshID, String nodeID, int crossFlag, int lightFlag, String geom, List<String> crossLinkList,
				String mainNodeID, List<String> subNodeList, List<String> linkList) {
		// 使用原始NI数据生成Node，各字段数据未规整
		super();
		this.meshID = meshID;
		this.nodeID = nodeID;
		this.crossFlag = crossFlag;
		this.lightFlag = lightFlag;
		this.geom = geom;
		this.crossLinkList = crossLinkList;
		this.mainNodeID = mainNodeID;
		this.subNodeList = subNodeList;
		this.linkList = linkList;
		
		this.enterCrossLinkList = new ArrayList<String>();
		this.leaveCrossLinkList = new ArrayList<String>();
		this.enterNodeLinkList = new ArrayList<String>();
		this.leaveNodeLinkList = new ArrayList<String>();
	}

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getMeshID() {
		return meshID;
	}

	public void setMeshID(String meshID) {
		this.meshID = meshID;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public int getCrossFlag() {
		return crossFlag;
	}

	public void setCrossFlag(int crossFlag) {
		this.crossFlag = crossFlag;
	}

	public int getLightFlag() {
		return lightFlag;
	}

	public void setLightFlag(int lightFlag) {
		this.lightFlag = lightFlag;
	}

	public List<String> getCrossLinkList() {
		return crossLinkList;
	}

	public void setCrossLinkList(List<String> crossLinkList) {
		this.crossLinkList = crossLinkList;
	}

	public String getMainNodeID() {
		return mainNodeID;
	}

	public void setMainNodeID(String mainNodeID) {
		this.mainNodeID = mainNodeID;
	}

	public List<String> getSubNodeList() {
		return subNodeList;
	}

	public void setSubNodeList(List<String> subNodeList) {
		this.subNodeList = subNodeList;
	}

	public List<String> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<String> linkList) {
		this.linkList = linkList;
	}

	public List<String> getEnterCrossLinkList() {
		return enterCrossLinkList;
	}

	public void setEnterCrossLinkList(List<String> enterCrossLinkList) {
		this.enterCrossLinkList = enterCrossLinkList;
	}

	public List<String> getLeaveCrossLinkList() {
		return leaveCrossLinkList;
	}

	public void setLeaveCrossLinkList(List<String> leaveCrossLinkList) {
		this.leaveCrossLinkList = leaveCrossLinkList;
	}

	public List<String> getEnterNodeLinkList() {
		return enterNodeLinkList;
	}

	public void setEnterNodeLinkList(List<String> enterNodeLinkList) {
		this.enterNodeLinkList = enterNodeLinkList;
	}

	public List<String> getLeaveNodeLinkList() {
		return leaveNodeLinkList;
	}

	public void setLeaveNodeLinkList(List<String> leaveNodeLinkList) {
		this.leaveNodeLinkList = leaveNodeLinkList;
	}
	
	
	
}
