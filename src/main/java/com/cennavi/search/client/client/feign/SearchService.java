package com.cennavi.search.client.client.feign;

import com.cennavi.search.client.client.feign.fallback.SearchServiceFallbackFactory;
import com.cennavi.search.client.model.SearchDto;
import com.cennavi.search.common.ESPageResult;
import com.cennavi.system.common.ServiceNameConstants;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author zlt
 */

@FeignClient(name = ServiceNameConstants.SEARCH_SERVICE, fallbackFactory = SearchServiceFallbackFactory.class, decode404 = true)
public interface SearchService {
    /**
     * 查询文档列表
     * @param indexName 索引名
     * @param searchDto 搜索Dto
     */
    @PostMapping(value = "/search/{indexName}")
    ESPageResult<JsonNode> strQuery(@PathVariable("indexName") String indexName, @RequestBody SearchDto searchDto);
}
