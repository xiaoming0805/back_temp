package com.cennavi.modules.baseimport.beans;


import com.cennavi.core.common.MyTable;

/**
 * Created by sunpengyan on 2017/7/5.
 * rtic信息表
 */
@MyTable("base_rtic")
public class BaseRtic {
    //RTICID  ROADNAME  SJNAME  EJNAME  LEN  KIND   X1  Y1  X2  Y2
    //CARNUM  WIDTH  SPEEDLIMIT  DIRECTION  FuncClass
    private String id;
    private String name;
    private String startName;
    private String endName;
    private float length;
    private String kind;
    private String width;
    private String speedLimit;
    private String direction;
    private String startpoint;//起点经纬度 以空格分隔
    private String endpoint;
    private String center;//中心点
    private String geometry; //geojson格式

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(String speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }
}
