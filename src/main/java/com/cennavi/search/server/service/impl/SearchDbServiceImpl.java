package com.cennavi.search.server.service.impl;

import com.cennavi.search.client.model.SearchDto;
import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.common.SearchBuilder;
import com.cennavi.search.logs.model.Audit;
import com.cennavi.search.logs.service.IAuditService;
import com.cennavi.search.server.service.ISearchService;
import com.cennavi.search.server.service.SearchDbService;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.common.WebPageResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用搜索
 *
 * @author zlt
 * @date 2019/4/24
 */
@Service
public class SearchDbServiceImpl implements SearchDbService {
    @Autowired
    private IAuditService auditService;

    @Override
    public ESPageResult<Audit> strQuery(Map<String, Object> params) throws IOException {
        Integer curPage = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        ESPageResult<Audit> pagelist = auditService.getPage(curPage,limit, params);
        return pagelist;
    }
}
