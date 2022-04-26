package com.cennavi.modules.baseimport.dao;

import com.cennavi.core.common.dao.BaseDao;
/**
 * 样例dao
 */
public interface BaseImportDao extends BaseDao<Object> {

    void updateAreaTableGeom();

    void updateRoadTableGeom();

    void updateRticTableGeom();

    void updateLinkTableGeom();

}
