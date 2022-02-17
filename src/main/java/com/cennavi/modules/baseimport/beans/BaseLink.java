package com.cennavi.modules.baseimport.beans;


import com.cennavi.core.common.MyTable;



/**
 * Link entity. @author MyEclipse Persistence Tools
 */
@MyTable("base_link")
public class BaseLink implements java.io.Serializable {


	// Fields

	private String id;
	private String geometry;
	private String kind;
	private String startName;
	private String endName;
	private short direction;
	private double width;
	private double length;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getStartName() {
		return startName;
	}

	public void setStartName(String startName) {
		this.startName = startName;
	}

	public String getEndName() {
		return endName;
	}

	public void setEndName(String endName) {
		this.endName = endName;
	}

	public short getDirection() {
		return direction;
	}

	public void setDirection(short direction) {
		this.direction = direction;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}





}