package com.cennavi.modules.sample.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.modules.sample.beans.SampleBean;

import java.util.List;
import java.util.Map;

/**
 * 样例dao
 * Created by sunpengyan on 2021/1/5.
 */
public interface SampleDao extends BaseDao<SampleBean> {

    /**
     * 根据名称模糊查询sample
     * @param name 名称
     * @return List<SampleBean>
     */
    List<SampleBean> listByName(String name);
    List<Map<String,Object>> listByName1(String name);

    List<Map<String,Object>> listCensus();
}
