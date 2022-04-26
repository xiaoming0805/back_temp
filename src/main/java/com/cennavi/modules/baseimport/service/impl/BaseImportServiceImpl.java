package com.cennavi.modules.baseimport.service.impl;

import com.cennavi.modules.baseimport.beans.*;
import com.cennavi.modules.baseimport.dao.BaseImportDao;
import com.cennavi.modules.baseimport.service.BaseImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
