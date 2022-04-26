package com.cennavi.modules.baseimport.dao.impl;

import com.cennavi.modules.baseimport.dao.BaseImportDao;
import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 */
@Repository
public class BaseImportImpl extends BaseDaoImpl<Object> implements BaseImportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void updateAreaTableGeom() {
        String sql = "update base_area set geom=st_geomfromtext(geometry);";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void updateRoadTableGeom() {
        String sql = " update base_road set center_geom=st_geomfromgeojson(center),geom=st_geomfromgeojson(geometry)";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void updateRticTableGeom() {
        String sql = "update base_rtic set center_geom=st_geomfromgeojson(center),geom=st_geomfromgeojson(geometry)";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void updateLinkTableGeom() {
        String sql = "update base_link set geom=st_geomfromgeojson(geometry)";
        jdbcTemplate.execute(sql);
    }
}
