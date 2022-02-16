package com.cennavi.modules.pbf.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by sunpengyan on 2022/2/15.
 */
@Repository
public class PbfDaoImpl implements PbfDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getDeviceListPbf(String wktStr) {
        String sql = "select *,st_astext(geom) as wkt from bayonet_device where st_intersects(st_geomfromtext(?),geom) ";
        /*List<Object> params = new ArrayList<>();
        params.add(wktStr);*/
        try {
            return jdbcTemplate.queryForList(sql, wktStr);
        } catch (Exception e) {//数据库版本不同时，st_geomfromtext转换的时候有的要加4326，否则会报错
            sql = "select *,st_astext(geom) as wkt from bayonet_device where st_intersects(st_geomfromtext(?,4326),geom) ";
            return jdbcTemplate.queryForList(sql, wktStr);
        }
    }
}
