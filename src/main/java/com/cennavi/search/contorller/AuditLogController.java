package com.cennavi.search.contorller;

import com.cennavi.search.client.client.service.IQueryService;
import com.cennavi.search.client.model.SearchDto;
import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.logs.model.Audit;
import com.cennavi.search.server.service.SearchDbService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * 审计日志
 *
 * @author zlt
 * @date 2020/2/4
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
@RestController
@RequestMapping("/auditlogs")
public class AuditLogController {
    @Autowired
    private IQueryService queryService;
    @Autowired
    private SearchDbService queryDbService;
    public AuditLogController(IQueryService queryService) {
        this.queryService = queryService;
    }
    @ApiOperation(value = "审计日志全文搜索列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "queryStr", value = "搜索关键字", dataType = "String")
    })
    @GetMapping(value = "/auditLog")
    public ESPageResult<JsonNode> getPage(SearchDto searchDto) throws IOException {
        searchDto.setIsHighlighter(true);
        searchDto.setSortCol("timestamp");
        return queryService.strQuery("audit-log-*", searchDto);
    }

    @GetMapping(value = "/auditLogDb")
    public ESPageResult<Audit> getPageDb(@RequestParam Map<String, Object> params) throws IOException {
        return queryDbService.strQuery(params);
    }
}
