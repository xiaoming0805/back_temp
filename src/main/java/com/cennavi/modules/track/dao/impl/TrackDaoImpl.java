package com.cennavi.modules.track.dao.impl;

import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import com.cennavi.modules.track.beans.Track;
import com.cennavi.modules.track.dao.TrackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpengyan on 2021/1/5.
 */
@Repository
public class TrackDaoImpl extends BaseDaoImpl<Track> implements TrackDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Track> getTrack(String name, String stime, String etime) {
        String sql = "SELECT * FROM track where realname=? and createtime>=? and createtime<=? order by createtime ";
        List<Object> params = new ArrayList<>();
        params.add(name);
        params.add(stime);
        params.add(etime);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Track.class), params.toArray());
    }
}
