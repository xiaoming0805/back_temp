package com.cennavi.search.client.client.service.impl;

import com.cennavi.search.client.client.feign.AggregationService;
import com.cennavi.search.client.client.feign.SearchService;
import com.cennavi.search.client.client.service.IQueryService;
import com.cennavi.search.client.model.LogicDelDto;
import com.cennavi.search.client.model.SearchDto;
import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.server.service.IAggregationService;
import com.cennavi.search.server.service.ISearchService;
import com.cennavi.system.common.WebPageResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * 搜索客户端Service
 *
 * @author zlt
 * @date 2019/4/24
 */
@Service
public class QueryServiceImpl implements IQueryService {
//    @Resource
//    private SearchService searchService;
//
//    @Resource
//    private AggregationService aggregationService;
    @Autowired
    private ISearchService searchService;
    @Autowired
    private IAggregationService aggregationService;
    @Override
    public ESPageResult<JsonNode> strQuery(String indexName, SearchDto searchDto) throws IOException {
        return strQuery(indexName, searchDto, null);
    }

    @Override
    public ESPageResult<JsonNode> strQuery(String indexName, SearchDto searchDto, LogicDelDto logicDelDto) throws IOException {
        setLogicDelQueryStr(searchDto, logicDelDto);
        return searchService.strQuery(indexName, searchDto);
    }

    /**
     * 拼装逻辑删除的条件
     * @param searchDto 搜索dto
     * @param logicDelDto 逻辑删除dto
     */
    private void setLogicDelQueryStr(SearchDto searchDto, LogicDelDto logicDelDto) {
        if (logicDelDto != null
                &&(!"".equals(logicDelDto.getLogicDelField())&&logicDelDto.getLogicDelField()!=null)
                &&(!"".equals(logicDelDto.getLogicNotDelValue())&&logicDelDto.getLogicNotDelValue()!=null)) {
            String result;
            //搜索条件
            String queryStr = searchDto.getQueryStr();
            //拼凑逻辑删除的条件
            String logicStr = logicDelDto.getLogicDelField() + ":" + logicDelDto.getLogicNotDelValue();
            if (!"".equals(queryStr)&&queryStr!=null) {
                result = "(" + queryStr + ") AND " + logicStr;
            } else {
                result = logicStr;
            }
            searchDto.setQueryStr(result);
        }
    }

    /**
     * 访问统计聚合查询
     * @param indexName 索引名
     * @param routing es的路由
     */
    @Override
    public Map<String, Object> requestStatAgg(String indexName, String routing) throws IOException {
        return aggregationService.requestStatAgg(indexName, routing);
    }
}
