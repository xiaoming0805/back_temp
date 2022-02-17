package com.cennavi.modules.baseimport.service;

import com.cennavi.core.common.PageResult;
import com.cennavi.modules.baseimport.beans.*;
import com.cennavi.modules.sample.beans.SampleBean;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

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
