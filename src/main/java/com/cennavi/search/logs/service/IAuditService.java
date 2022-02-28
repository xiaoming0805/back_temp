package com.cennavi.search.logs.service;


import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.logs.model.Audit;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * 审计日志接口
 *
 * @author zlt
 * @date 2020/2/3
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
public interface IAuditService {
    void save(Audit audit);

    ESPageResult<Audit> getPage(Integer curPage, Integer limit, Map<String, Object> params);
}
