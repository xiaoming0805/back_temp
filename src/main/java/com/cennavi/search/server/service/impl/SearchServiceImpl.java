package com.cennavi.search.server.service.impl;

import com.cennavi.search.client.model.SearchDto;
import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.common.SearchBuilder;
import com.cennavi.search.server.service.ISearchService;
import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 通用搜索
 *
 * @author zlt
 * @date 2019/4/24
 */
@Service
public class SearchServiceImpl implements ISearchService {
    private final RestHighLevelClient client;

    public SearchServiceImpl(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * StringQuery通用搜索
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     * @return
     */
    @Override
    public ESPageResult<JsonNode> strQuery(String indexName, SearchDto searchDto) throws IOException {
        return SearchBuilder.builder(client, indexName)
                .setStringQuery(searchDto.getQueryStr())
                .addSort(searchDto.getSortCol(), SortOrder.DESC)
                .setIsHighlight(searchDto.getIsHighlighter())
                .getPage(searchDto.getPage(), searchDto.getLimit());
    }
}
