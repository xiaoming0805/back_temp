package com.cennavi.search.server.service;

import com.cennavi.search.client.model.SearchDto;
import com.cennavi.search.common.ESPageResult;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * @author zlt
 * @date 2019/4/24
 */
public interface ISearchService {
    /**
     * StringQuery通用搜索
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     * @return
     */
    ESPageResult<JsonNode> strQuery(String indexName, SearchDto searchDto) throws IOException;
}
