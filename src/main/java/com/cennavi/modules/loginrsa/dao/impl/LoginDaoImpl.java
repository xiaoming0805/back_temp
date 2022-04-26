package com.cennavi.modules.loginrsa.dao.impl;

import com.cennavi.modules.loginrsa.dao.LoginDao;
import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunpengyan on 2021/1/5.
 */
@Repository
public class LoginDaoImpl extends BaseDaoImpl<Object> implements LoginDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Map<String, Object>> getUser(String username, String password) {
        List<String> params = new ArrayList<>();
        String sql = "select * from sys_user where username=? ";
        params.add(username);
        return jdbcTemplate.queryForList(sql,params.toArray());
    }
}
