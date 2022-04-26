package com.cennavi.modules.inoutflow.service.impl;

import com.cennavi.modules.inoutflow.dao.InoutFlowDao;
import com.cennavi.modules.inoutflow.service.InoutFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 样例service
 */
@Service
public class InoutFlowServiceImpl implements InoutFlowService {

    @Autowired
    private InoutFlowDao inoutFlowDao;


    @Override
    public void makeAndSaveFlowODBySql(int areatype, String date) {
        //算法逻辑：1.计算出每一辆车最早和最晚的所在区域(或道路)，及每一辆车出现过的区域(或道路)
        // 遍历后者，分别和前者的区域组合成od关系
        inoutFlowDao.makeAndSaveFlowODBySql(areatype, date);
    }

    @Override
    public List<Map<String, Object>> getFlowOD(int type, String date, String code) {
        List<Map<String, Object>> a = inoutFlowDao.getFlowOD(type, date, code);
        System.out.println(a.size());
        return a;
    }
}
