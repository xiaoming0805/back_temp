package com.cennavi.modules.inoutflow.controller;

import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.modules.inoutflow.beans.OdFlow;
import com.cennavi.modules.inoutflow.service.InoutFlowService;
import com.cennavi.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = {"区域流入流出数据、OD数据"},value = "inoutflow")
@RestController
@RequestMapping("/inoutflow")
public class InoutFlowController extends ResponseUtils {
    private Logger logger = LoggerFactory.getLogger(InoutFlowController.class);
    @Autowired
    private InoutFlowService inoutFlowService;


    /**
     * 说明
     * 统计类接口查询-1
     * 场景：使用多表查询或者 sum count 出来的数据 表中没有相应的字段
     * 例如多表查询且使用 jdbcTemplate 返回了 List<Map<String,Object>> 或者 Map<String,Object> 类型的数据，生成api标准接口文档时
     * 可以 使用 ResponseUtils 类中 toBean 方法 转化为类 并且返回
     * 缺点：写大量的 VO 类文件 影响效率
     * 如果不想写大量的类文件。可以参考例子，允许简便写法
     */
    @ApiOperation(value = "生成某天行政区划流入流出数据的接口,项目中需要根据需求作为定时任务")
    @ApiImplicitParams({})
    @GetMapping("/calData")
    public ResultObj calData() {
        String date = DateUtils.DateFormatUnit.DATE.getDateStr(DateUtils.addDays(new Date(), -1));
        date = "2022-02-20";//模板代码中把日期写死、后续根据项目修改
        inoutFlowService.makeAndSaveFlowODBySql(2,date);//行政区域od,type=2目前代表行政区划，后续多个可自行修改
        return success();
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
    @ApiOperation(value = "区域OD数据查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", required=true, dataType = "String", name = "date", value = "时间", example = "2021-11-06"),
            @ApiImplicitParam(paramType = "query", required=true,dataType = "String", name = "code", value = "区域编码", example = "130725"),
            @ApiImplicitParam(paramType = "query", required=true, dataType = "String", name = "odType", value = "类型 1流入、2流出", example = "1")
    })
    @GetMapping("/getOdFlow")
    public ResultObj<List<OdFlow>> getOdFlow(@RequestParam(value = "date") String date, String code, Integer odType) {
        List<Map<String,Object>> list = inoutFlowService.getFlowOD(odType,code,date);
        List<OdFlow> odFlows = toBean(list,OdFlow.class);
        return success(odFlows);
    }





}
