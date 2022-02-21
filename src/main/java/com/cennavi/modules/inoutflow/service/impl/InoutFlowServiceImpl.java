package com.cennavi.modules.inoutflow.service.impl;

import com.cennavi.core.common.PageResult;
import com.cennavi.modules.inoutflow.dao.InoutFlowDao;
import com.cennavi.modules.inoutflow.service.InoutFlowService;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.dao.SampleDao;
import com.cennavi.modules.sample.service.SampleService;
import com.cennavi.modules.track.beans.Track;
import com.cennavi.modules.track.dao.TrackDao;
import com.cennavi.utils.DateUtils;
import com.cennavi.utils.GeomtryUtils;
import com.cennavi.utils.PositionUtils;
import com.cennavi.utils.SendUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
