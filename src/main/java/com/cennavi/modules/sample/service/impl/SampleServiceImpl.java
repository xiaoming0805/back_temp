package com.cennavi.modules.sample.service.impl;

import com.cennavi.core.common.PageResult;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.dao.SampleDao;
import com.cennavi.modules.sample.service.SampleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 样例service
 * Created by sunpengyan on 2021/1/5.
 */
@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleDao sampleDao;


    @Override
    public List<SampleBean> listByName(String name) {
        return sampleDao.listByName(name);
    }

    @Override
    public List<Map<String,Object>> listByName1(String name) {
        return sampleDao.listByName1(name);
    }

    @Override
    public void save(SampleBean bean) {
        sampleDao.save(bean);
    }

    @Override
    public void update(SampleBean bean) {
        sampleDao.update(bean);
    }

    @Override
    public void delete(String id) {
        sampleDao.delete(id);
    }

    @Override
    public PageResult<SampleBean> findByPage(String name, int pageNo, int pageSize) {
        Map<String, String> where = new HashMap<>();
        if(StringUtils.isNotBlank(name)) {
            where.put("name","like"+name);
        }
        return sampleDao.findByPage(pageNo, pageSize, where);
    }

    @Override
    public SampleBean findById(String id) {
        return sampleDao.findById(id);
    }

    public List<SampleBean> findAll() {
        return sampleDao.findAll();
    }

    @Override
    public List<Map<String,Object>> listCensus() {
        return sampleDao.listCensus();
    }

    @Override
    public void save(String name, Integer age, String code) {

    }
}
