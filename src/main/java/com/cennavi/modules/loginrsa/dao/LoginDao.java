package com.cennavi.modules.loginrsa.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.track.beans.Track;

import java.util.List;
import java.util.Map;

/**
 * 样例dao
 */
public interface LoginDao extends BaseDao<Object> {

    /**
     * 根据用户名密码查询用户
     */
    List<Map<String,Object>> getUser(String username,String password);
}
