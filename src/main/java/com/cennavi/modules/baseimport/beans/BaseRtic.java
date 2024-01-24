package com.cennavi.modules.baseimport.beans;


import com.cennavi.core.common.MyTable;
import lombok.Data;

/**
 * Created by sunpengyan on 2017/7/5.
 * rtic信息表
 */
@Data
@MyTable("base_rtic")
public class BaseRtic {
    //RTICID  ROADNAME  SJNAME  EJNAME  LEN  KIND   X1  Y1  X2  Y2
    //CARNUM  WIDTH  SPEEDLIMIT  DIRECTION  FuncClass
    private String id;
    private String name;
    private String startname;
    private String endname;
    private float length;
    private String kind;
    private String width;
    private String speedlimit;
    private String direction;
    private String startpoint;//起点经纬度 以空格分隔
    private String endpoint;
    private String center;//中心点
    private String geometry; //geojson格式

}
