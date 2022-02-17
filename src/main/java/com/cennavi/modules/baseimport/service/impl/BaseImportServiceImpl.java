package com.cennavi.modules.baseimport.service.impl;

import com.cennavi.core.common.PageResult;
import com.cennavi.modules.baseimport.beans.*;
import com.cennavi.modules.baseimport.dao.BaseImportDao;
import com.cennavi.modules.baseimport.service.BaseImportService;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.dao.SampleDao;
import com.cennavi.modules.sample.service.SampleService;
import com.cennavi.modules.track.beans.Track;
import com.cennavi.modules.track.dao.TrackDao;
import com.cennavi.modules.track.service.TrackService;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 样例service
 */
@Service
public class BaseImportServiceImpl implements BaseImportService {

    @Autowired
    private BaseImportDao importDao;



    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void batchSaveArea(List<BaseArea> list) {
        importDao.deleteAll(BaseArea.class);
        importDao.batchSave(list,BaseArea.class);
        importDao.updateAreaTableGeom();
    }

    @Override
    public void batchSaveRtic(List<BaseRtic> baseRtics) {
        importDao.batchSave(baseRtics,BaseRtic.class);
        importDao.updateRticTableGeom();
    }

    @Override
    public void batchSaveAreaRoadRtic(List<BaseAreaRoadRtic> list) {
        importDao.batchSave(list,BaseAreaRoadRtic.class);
    }

    @Override
    public void batchSaveRticLink(List<BaseRticLink> list){
        importDao.batchSave(list,BaseRticLink.class);
    }

    @Override
    public void batchSaveRoad(List<BaseRoad> baseRoads) {
        importDao.batchSave(baseRoads,BaseRoad.class);
        importDao.updateRoadTableGeom();
    }

    @Override
    public void batchSaveLinkList(List<BaseLink> list) {
        importDao.batchSave(list, BaseLink.class);
        importDao.updateLinkTableGeom();
    }
}
