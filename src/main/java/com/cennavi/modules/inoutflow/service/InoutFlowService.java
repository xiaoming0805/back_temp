package com.cennavi.modules.inoutflow.service;

import java.util.List;
import java.util.Map;

/**
 */
public interface InoutFlowService {

    /**
     * 生成并保存行政区划od,通过sql
     * @param type 1道路，2行政区划，3重点区域，4景区
     * @param date 日期 yyyy-MM-dd
     * @return
     */
    void makeAndSaveFlowODBySql(int type,String date);

    /**
     * 查询区域od
     * @param type 1流入、2流出
     * @param date 日期，yyyy-MM-dd
     * @param code 道路(重点道路)，行政区划，重点区域，景区 code
     */
    List<Map<String,Object>> getFlowOD(int type, String date, String code);
}
