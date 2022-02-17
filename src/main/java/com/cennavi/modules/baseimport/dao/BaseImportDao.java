package com.cennavi.modules.baseimport.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.track.beans.Track;

import java.util.List;
import java.util.Map;

/**
 * 样例dao
 */
public interface BaseImportDao extends BaseDao<Object> {

    void updateAreaTableGeom();

    void updateRoadTableGeom();

    void updateRticTableGeom();

    void updateLinkTableGeom();

}
