package com.cennavi.search.client.client.feign.fallback;

import com.cennavi.search.client.client.feign.SearchService;
import com.cennavi.search.common.ESPageResult;
import com.cennavi.system.common.WebPageResult;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * searchService降级工场
 *
 * @author zlt
 */
@Slf4j
public class SearchServiceFallbackFactory implements FallbackFactory<SearchService> {
    @Override
    public SearchService create(Throwable throwable) {
        return (indexName, searchDto) -> {
            log.error("通过索引{}搜索异常:{}", indexName, throwable);
            return ESPageResult.<JsonNode>builder().build();
        };
    }
}
