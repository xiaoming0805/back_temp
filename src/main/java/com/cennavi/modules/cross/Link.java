package com.cennavi.modules.cross;


import com.cennavi.core.common.IgnoreColumn;
import com.cennavi.core.common.MyTable;

import java.util.List;

@MyTable("link")
public class Link {
	private String id;
	private int kind; // 道路等级
	private String sNodeID;
	private String eNodeID;
	private int length; // 单位：米
	private String geom; // wkt
	private String geomoffset; // 向外侧偏移后的经纬度
	private String name; // link的道路名称
	private int width;

	@IgnoreColumn("")
	private String meshID;
	@IgnoreColumn("")
	private List<String> linkClass; // 道路类型（IC、JCT等，一条Link可有多个道路类型）
	@IgnoreColumn("")
	private int laneNum;
	@IgnoreColumn("")
	private double speedLimit; // 单位：km/h

	// Link的空间四至
	@IgnoreColumn("")
	private double maxLon;
	@IgnoreColumn("")
	private double minLon;
	@IgnoreColumn("")
	private double maxLat;
	@IgnoreColumn("")
	private double minLat;
	@IgnoreColumn("")
	private int FRC;
	
	public Link(String meshID, String linkID, int linkKind, List<String> linkClass, String sNodeID, String eNodeID, int FRC,
				int length, int laneNum, double speedLimit, String geom, double maxLon, double minLon, double maxLat,
				double minLat, String name, int width) {
		super();
		this.meshID = meshID;
		this.id = linkID;
		this.kind = linkKind;
		this.linkClass = linkClass;
		this.sNodeID = sNodeID;
		this.eNodeID = eNodeID;
		this.FRC = FRC;
		this.length = length;
		this.laneNum = laneNum;
		this.speedLimit = speedLimit;
		this.geom = geom;
		this.maxLon = maxLon;
		this.minLon = minLon;
		this.maxLat = maxLat;
		this.minLat = minLat;
		this.name = name;
		this.width = width;
	}

	public String getGeompy() {
		return geomoffset;
	}

	public void setGeompy(String geompy) {
		this.geomoffset = geompy;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getFRC() {
		return FRC;
	}

	public void setFRC(int fRC) {
		FRC = fRC;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMaxLon() {
		return maxLon;
	}

	public void setMaxLon(double maxLon) {
		this.maxLon = maxLon;
	}

	public double getMinLon() {
		return minLon;
	}

	public void setMinLon(double minLon) {
		this.minLon = minLon;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
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

	public String getLinkID() {
		return id;
	}

	public void setLinkID(String linkID) {
		this.id = linkID;
	}

	public int getLinkKind() {
		return kind;
	}

	public void setLinkKind(int linkKind) {
		this.kind = linkKind;
	}

	public List<String> getLinkClass() {
		return linkClass;
	}

	public void setLinkClass(List<String> linkClass) {
		this.linkClass = linkClass;
	}

	public String getsNodeID() {
		return sNodeID;
	}

	public void setsNodeID(String sNodeID) {
		this.sNodeID = sNodeID;
	}

	public String geteNodeID() {
		return eNodeID;
	}

	public void seteNodeID(String eNodeID) {
		this.eNodeID = eNodeID;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLaneNum() {
		return laneNum;
	}

	public void setLaneNum(int laneNum) {
		this.laneNum = laneNum;
	}

	public double getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}


}
