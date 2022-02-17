package com.cennavi.modules.baseimport.beans;


import com.cennavi.core.common.MyTable;

/**
 * Created by sunpengyan on 2017/7/5.
 * 道路对应表
 */
@MyTable("base_road")
public class BaseRoad {
   // road_id	road_type	road_kind	road_name	alias_name	start_name	end_name
   // direction	road_length	road_center	road_geometry	invalid
    private String id;
    private String type;
    private String kind;
    private String name;
    private String startname;
    private String endname;
    private String direction;  //1238属于上行，4567下行
    private double length;
    private String center;
    private String geometry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartname() {
        return startname;
    }

    public void setStartname(String startname) {
        this.startname = startname;
    }

    public String getEndname() {
        return endname;
    }

    public void setEndname(String endname) {
        this.endname = endname;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
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
