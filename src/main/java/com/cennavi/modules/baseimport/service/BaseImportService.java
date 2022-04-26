package com.cennavi.modules.baseimport.service;

import com.cennavi.modules.baseimport.beans.*;

import java.util.List;

/**
 */
public interface BaseImportService {

    void batchSaveArea(List<BaseArea> list);

    void batchSaveRoad(List<BaseRoad> baseRoads);

    void batchSaveRtic(List<BaseRtic> baseRtics);

    void batchSaveAreaRoadRtic(List<BaseAreaRoadRtic> list);

    void batchSaveRticLink(List<BaseRticLink> list);

    void batchSaveLinkList(List<BaseLink> list);

}
