package com.cennavi.search.client.client.feign.fallback;

import com.cennavi.search.client.client.feign.AggregationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;


/**
 * searchService降级工场
 *
 * @author zlt
 */
@Slf4j
@Component
public class AggregationServiceFallbackFactory implements FallbackFactory<AggregationService> {
    @Override
    public AggregationService create(Throwable throwable) {
        return (indexName, routing) -> {
            log.error("通过索引{}搜索异常:{}", indexName, throwable);
            return new HashMap<>();
        };
    }
}
