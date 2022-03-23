package com.cennavi.modules.sample.service;

import com.cennavi.core.common.PageResult;
import com.cennavi.modules.sample.beans.SampleBean;

import java.util.List;
import java.util.Map;

/**
 * Created by sunpengyan on 2021/1/5.
 */
public interface SampleService {

    /**
     * 根据名称模糊查询sample
     * @param name 名称
     * @return List<SampleBean>
     */
    List<SampleBean> listByName(String name);
    List<Map<String,Object>> listByName1(String name);

    /**
     * 保存
     * @param bean SampleBean
     */
    void save(SampleBean bean);

    /**
     * 修改
     * @param bean SampleBean
     */
    void update(SampleBean bean);

    /**
     * 修改
     * @param id id
     */
    void delete(String id);

    /**
     * 分页查询
     * @param pageNo 页码，第几页
     * @param pageSize 每页的数量
     * @param name 名称
     * @return
     */
    PageResult<SampleBean> findByPage(String name, int pageNo, int pageSize);

    /**
     * 根据id查询
     */
    SampleBean findById(String id);

    /**
     * 查询所有
     * @return List<SampleBean>
     */
    List<SampleBean> findAll();

    List<Map<String,Object>> findByExport();
    /**
     * 查询所有
     * @return
     */
    List<Map<String,Object>> listCensus();

    /**
     *
     * @param name
     * @param age
     * @param code
     */
    void save(String name,Integer age,String code);

    void batchSave(List<SampleBean> list);

    void batchSaveByStr(List<String> datas);
}

