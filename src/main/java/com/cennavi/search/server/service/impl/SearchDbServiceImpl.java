package com.cennavi.search.server.service.impl;

import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.logs.model.Audit;
import com.cennavi.search.logs.service.IAuditService;
import com.cennavi.search.server.service.SearchDbService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

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
