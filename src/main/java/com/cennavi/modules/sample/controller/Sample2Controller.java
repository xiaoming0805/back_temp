package com.cennavi.modules.sample.controller;

import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.service.SampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = {"Demo示例2"},value = "sample2")
@RestController
@RequestMapping("/sample2")
public class Sample2Controller extends ResponseUtils {
    private Logger logger = LoggerFactory.getLogger(Sample2Controller.class);
    @Autowired
    private SampleService sampleService;

    /**
     * 说明
     * 多用于单表查询，api文档中可以直接直接返回bean类中的注释说明
     * 适合使用脚手架封装的basedao 操作数据库
     */
    @ApiOperation(value = "单表查询接口_01")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "name", value = "姓名", example = "张三")
    })
    @GetMapping("/listSampleByName")
    public ResultObj listSampleByName(@RequestParam(value = "name") String name) {
        List<SampleBean> list = sampleService.listByName(name);
        return success(list);
    }

    /**
     * 说明
     * 统计类接口查询-1
     * 场景：使用多表查询或者 sum count 出来的数据 表中没有相应的字段
     * 例如多表查询且使用 jdbcTemplate 返回了 List<Map<String,Object>> 或者 Map<String,Object> 类型的数据，生成api标准接口文档时
     * 可以 使用 ResponseUtils 类中 toBean 方法 转化为类 并且返回
     * 缺点：写大量的 VO 类文件 影响效率
     * 如果不想写大量的类文件。可以参考例子，允许简便写法
     */
//    @ApiOperation(value = "统计类查询接口_02")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "name", value = "姓名", example = "张三")
//    })
//    @GetMapping("/listCensus")
//    public ResultObj<List<ListCensusVO>> listCensus(@RequestParam(value = "name") String name) {
//        List<Map<String, Object>> list = sampleService.listCensus();
//        List<ListCensusVO> json = toBean(list, ListCensusVO.class);
//        return success(json);
//    }

    /**
     * 说明
     * 允许查询统计类接口 简便写法
     * 在@ApiOperation 注解中加入 notes 属性 把字段说明写进去
     * 缺点：api文件不友好
     */
    @ApiOperation(value = "统计类查询接口_03", notes = "[{\"myid\":\"asdad\",\"myname\":\"张三\"}]")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "name", value = "姓名", example = "张三")
    })
    @GetMapping("/listCensusEasy")
    public ResultObj listCensusEasy(@RequestParam(value = "name") String name) {
        List<Map<String, Object>> list = sampleService.listCensus();
        return success(list);
    }

    /**
     * 说明
     * @ApiImplicitParams required字段标识字段是否必传
     * return success() 表示处理 void 方法默认成功 如果失败会自动调用error() 方法
     */
    @ApiOperation(value = "保存_01")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "name", value = "姓名", example = "张三", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "age", value = " 年龄", example = "20", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "code", value = "编码", example = "zhangs")
    })
    @PostMapping("/save")
    public ResultObj save(@RequestParam(value = "name", required = true) String name,
                          @RequestParam(value = "age", required = true) Integer age,
                          @RequestParam(value = "code", required = false) String code) {
        sampleService.save(name, age, code);
        return success();
    }

    @ApiOperation(value = "保存_02")
    @PostMapping("/save1")
    public ResultObj save1(SampleBean bean) {
        sampleService.save(bean);
        return success();
    }


    //
//    /**
//     * 根据名称模糊查询sample
//     * @param name 名称
//     * @return {"errcode":0,"errmsg":"success","data":[{},{},...]}
//     */
//    @RequestMapping("/listSampleByName1")
//    public String listSampleByName1(@RequestParam(value = "name") String name) {
//        ResultObj ro;
//        try {
//            List list = sampleService.listByName(name);
//            ro = new ResultObj(list);
//        } catch (Exception e) {
//            logger.info(e.getMessage(),e);
//            ro = new ResultObj(false, e.getMessage());
//        }
//        JacksonUtils.objectToJson(ro, new String[]{"id"});
//        return ro.toJsonString();
//    }
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
//    /**
//     * 查询所有
//     * @return {"errcode":0,"errmsg":"success","data":[{},{},...]}
//     */
//    @RequestMapping(value = "/findAll")
//    public String findAll(){
//        ObjectNode json;
//        try {
//            List<SampleBean> list = sampleService.findAll();
//            //json = JacksonUtils.packJson(list);
//            json = JacksonUtils.packJson(list, new String[]{"id"});
//        } catch (Exception e) {
//            logger.info(e.getMessage(),e);
//            json = JacksonUtils.packJsonErr(e.getMessage());
//        }
//        return JacksonUtils.objectToJson(json,new String[]{"id"});
//    }

}
