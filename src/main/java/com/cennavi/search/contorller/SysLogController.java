package com.cennavi.search.contorller;

import com.cennavi.search.client.client.service.IQueryService;
import com.cennavi.search.client.model.SearchDto;
import com.cennavi.search.common.ESPageResult;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 系统日志
 *
 * @author zlt
 */
@RestController
@RequestMapping("/syslogs")
public class SysLogController {
    @Autowired
    private IQueryService queryService;

    @ApiOperation(value = "系统日志全文搜索列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "queryStr", value = "搜索关键字", dataType = "String")
    })
    @GetMapping(value = "/sysLog")
    public ESPageResult<JsonNode> getPage(SearchDto searchDto) throws IOException {
        searchDto.setIsHighlighter(true);
        searchDto.setSortCol("timestamp");
        return queryService.strQuery("sys-log-*", searchDto);
    }
}
