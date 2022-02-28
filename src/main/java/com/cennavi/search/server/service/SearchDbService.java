package com.cennavi.search.server.service;

import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.logs.model.Audit;


import java.io.IOException;
import java.util.Map;

/**
 * @author zlt
 * @date 2019/4/24
 */
public interface SearchDbService {
    /**
     * StringQuery通用搜索
     * @return
     */
    ESPageResult<Audit> strQuery(Map<String, Object> params) throws IOException;
}
