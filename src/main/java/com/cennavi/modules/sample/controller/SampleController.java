package com.cennavi.modules.sample.controller;

import com.cennavi.core.common.PageResult;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.service.SampleService;
import com.cennavi.utils.JacksonUtils;
import com.cennavi.utils.ResultObj;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 *  工程模板样例controller
 * Created by sunpengyan on 2021/1/5.
 */
@RestController
@RequestMapping("/sample")
public class SampleController {
    private Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private SampleService sampleService;

//
    /**
     * 根据名称模糊查询sample
     * @param name 名称
     * @return {"errcode":0,"errmsg":"success","data":{"total":2,"rows":[{},{},...]}}
     */
    @RequestMapping("/listSampleByName")
    public String listSampleByName(@RequestParam(value = "name") String name) {
        ObjectNode json;
        try {
            List list = sampleService.listByName(name);
            json = JacksonUtils.packJson(list);
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            json = JacksonUtils.packJsonErr(e.getMessage());
        }
        return JacksonUtils.objectToJson(json,new String[]{});
    }

    /**
     * 根据名称模糊查询sample
     * @param name 名称
     * @return {"errcode":0,"errmsg":"success","data":[{},{},...]}
     */
    @RequestMapping("/listSampleByName1")
    public String listSampleByName1(@RequestParam(value = "name") String name) {
        ResultObj ro;
        try {
            List list = sampleService.listByName(name);
            ro = new ResultObj(list);
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            ro = new ResultObj(false, e.getMessage());
        }
        JacksonUtils.objectToJson(ro, new String[]{"id"});
        return ro.toJsonString();
    }
//
//    /**
//     * 新增
//     * @param bean bean
//     * @return {"errcode":0,"errmsg":"success"}
//     */
//    @RequestMapping(value = "/save")
//    public String save(SampleBean bean){
//        JSONObject json = JsonUtils.packOuterSuccessJson();
//        try {
//            //单元测试需要插入带id的测试数据，此时不需要设置id
//            if(bean.getId()==null) {
//                bean.setId(UUID.randomUUID().toString());
//            }
//            sampleService.save(bean);
//        } catch (Exception e) {
//            logger.info(e.getMessage(),e);
//            json = JsonUtils.packOuterErrJson(e.getMessage());
//        }
//        return JsonUtils.objectToJson(json, new String[]{"_id"});
//    }
//
//
//    /**
//     *  bean字段更新
//     * @param bean
//     * @return {"errcode":0,"errmsg":"success"}
//     */
//    @RequestMapping(value = "/update")
//    public String update(SampleBean bean){
//        JSONObject json = JsonUtils.packOuterSuccessJson();
//        try {
//            SampleBean sa = sampleService.findById(bean.getId());
//            if(sa!=null) {
//                sampleService.update(bean);
//            } else {
//                JsonUtils.packOuterErrJson("修改失败:id不存在");
//            }
//        } catch (Exception e) {
//            logger.info(e.getMessage(),e);
//            json = JsonUtils.packOuterErrJson(e.getMessage());
//        }
//        return JsonUtils.objectToJson(json,new String[]{});
//    }
//
//    /**
//     * 根据id删除对应数据
//     * @param id id
//     * @return {"errcode":0,"errmsg":"success"}
//     */
//    @RequestMapping(value = "/delete")
//    public String delete(String id){
//        JSONObject json = JsonUtils.packOuterSuccessJson();
//        try {
//            sampleService.delete(id);
//        } catch (Exception e) {
//            logger.info(e.getMessage(),e);
//            json = JsonUtils.packOuterErrJson(e.getMessage());
//        }
//        return JsonUtils.objectToJson(json,new String[]{});
//    }
//
//    /**
//     * 分页查询
//     * @param pageNum 页码 offset=(pageNum-1)*pageSize
//     * @param pageSize 每页的数量 limit
//     * @param name 名称
//     * @return {"errcode":0,"errmsg":"success"}
//     */
//    @RequestMapping(value = "/findByPage")
//    public String findByPage(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,
//                             @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
//                             @RequestParam(value = "pageSize",required = false) String name){
//        JSONObject json = JsonUtils.packOuterSuccessJson();
//        try {
//            PageResult<SampleBean> result = sampleService.findByPage(pageNum, pageSize, name);
//            List<SampleBean> list = result.getList();
//            JSONObject innerJson = JsonUtils.packJson(list);
//            innerJson.put("totalRow",result.getTotalRow());
//            innerJson.put("pageNum",pageNum);
//            innerJson.put("pageSize",pageSize);
//            json.put("data",innerJson);
//        } catch (Exception e) {
//            logger.info(e.getMessage(),e);
//            json = JsonUtils.packOuterErrJson(e.getMessage());
//        }
//        return JsonUtils.objectToJson(json,new String[]{});
//    }
//
    /**
     * 查询所有
     * @return {"errcode":0,"errmsg":"success","data":[{},{},...]}
     */
    @RequestMapping(value = "/findAll")
    public String findAll(){
        ObjectNode json;
        try {
            List<SampleBean> list = sampleService.findAll();
            //json = JacksonUtils.packJson(list);
            json = JacksonUtils.packJson(list, new String[]{"id"});
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            json = JacksonUtils.packJsonErr(e.getMessage());
        }
        return JacksonUtils.objectToJson(json,new String[]{"id"});
    }

}
