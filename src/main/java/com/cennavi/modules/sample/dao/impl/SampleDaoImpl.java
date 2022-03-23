package com.cennavi.modules.sample.dao.impl;

import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.dao.SampleDao;
import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpengyan on 2021/1/5.
 */
@Repository
public class SampleDaoImpl extends BaseDaoImpl<SampleBean> implements SampleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<SampleBean> listByName(String name) {
        String sql = "select id,name,code from sample where 1=1 ";
        List<Object> params = new ArrayList<>();
        if(StringUtils.isNotBlank(name)) {
            sql += "and name like ?";
            params.add("%"+name+"%");
        }
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SampleBean.class), params.toArray());
    }

    @Override
    public List<Map<String,Object>> listByName1(String name) {
        String sql = "select id,name,st_asgeojson(geom) as geom from sample where 1=1 ";
        List<Object> params = new ArrayList<>();
        if(StringUtils.isNotBlank(name)) {
            sql += "and name like ?";
            params.add("%"+name+"%");
        }
        return jdbcTemplate.queryForList(sql,params.toArray());
    }

    @Override
    public List<Map<String,Object>> listCensus() {
        String sql="select id as myid,name as myname from sample";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findByExport() {
        String sql = "select name,code from sample";
        return jdbcTemplate.queryForList(sql);
    }
}
