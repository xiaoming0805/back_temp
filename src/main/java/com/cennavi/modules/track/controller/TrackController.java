package com.cennavi.modules.track.controller;

import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.beans.vo.ListCensusVO;
import com.cennavi.modules.sample.service.SampleService;
import com.cennavi.modules.track.beans.Track;
import com.cennavi.modules.track.service.TrackService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

@Api(tags = {"轨迹优化"},value = "track")
@RestController
@RequestMapping("/track")
public class TrackController extends ResponseUtils {
    private Logger logger = LoggerFactory.getLogger(TrackController.class);
    @Autowired
    private TrackService trackService;


    /**
     * 说明
     * 统计类接口查询-1
     * 场景：使用多表查询或者 sum count 出来的数据 表中没有相应的字段
     * 例如多表查询且使用 jdbcTemplate 返回了 List<Map<String,Object>> 或者 Map<String,Object> 类型的数据，生成api标准接口文档时
     * 可以 使用 ResponseUtils 类中 toBean 方法 转化为类 并且返回
     * 缺点：写大量的 VO 类文件 影响效率
     * 如果不想写大量的类文件。可以参考例子，允许简便写法
     */
    @ApiOperation(value = "历史轨迹优化接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", required=true, dataType = "String", name = "name", value = "姓名", example = "阎长虹"),
            @ApiImplicitParam(paramType = "query", required=true,dataType = "String", name = "stime", value = "开始时间", example = "2021-12-23 14:33:27"),
            @ApiImplicitParam(paramType = "query", required=true, dataType = "String", name = "etime", value = "结束时间", example = "2021-12-23 14:55:17")
    })
    @GetMapping("/getTrack")
    public ResultObj<List<Track>> getTrack(@RequestParam(value = "name") String name,String stime,String etime) throws JsonProcessingException {
        List<Track> list = trackService.trackOptimize(name,stime,etime);
        return success(list);
    }





}
