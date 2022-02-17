package com.cennavi.modules.baseimport.beans;


import com.cennavi.core.common.MyTable;

/**
 * 辖区详情
 * Created by Admin on 2017-7-18.
 */
@MyTable("base_area")
public class BaseArea {
    private String id;//区域编号
    private String type;//区域类型
    private String name;//区域名称
    private String center;//中心点坐标
    private String geometry;//区域坐标

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
