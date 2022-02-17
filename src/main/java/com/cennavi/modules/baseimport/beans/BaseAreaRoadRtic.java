package com.cennavi.modules.baseimport.beans;


import com.cennavi.core.common.MyTable;

/**
 *  area road rtic 关系表
 */
@MyTable("base_area_road_rtic")
public class BaseAreaRoadRtic {
    private String area_id;
    private String road_id;
    private int seq_no;
    private String rtic_id;

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getRoad_id() {
        return road_id;
    }

    public void setRoad_id(String road_id) {
        this.road_id = road_id;
    }

    public int getSeq_no() {
        return seq_no;
    }

    public void setSeq_no(int seq_no) {
        this.seq_no = seq_no;
    }

    public String getRtic_id() {
        return rtic_id;
    }

    public void setRtic_id(String rtic_id) {
        this.rtic_id = rtic_id;
    }
}
