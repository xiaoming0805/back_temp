package com.cennavi.modules.loginrsa.service;

import com.cennavi.core.common.PageResult;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.track.beans.Track;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

/**
 */
public interface LoginService {

    /**
     *
     * @param username 轨迹人员的名字
     * @param password 轨迹的开始时间
     * @return
     */
    boolean login(String username, String password) ;


}
