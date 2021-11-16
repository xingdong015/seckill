package com.system.design.seckil.graphql.elasticsearch;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author     ：程征波
 * @date       ：Created in 2021/11/16 1:48 下午
 * @description：查询接口
 * @modified By：`
 * @version: 1.0
 */
@Component
public class ESearchServiceImpl implements ESearchService {

    @Resource
    RestHighLevelClient restHighLevelClient;

    @Override
    public Optional<Object> searchSimple(String tableName, QueryBuilder queryBuilder) throws Exception {
        //todo 抽象
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(tableName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().trackTotalHits(true);
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List list = new ArrayList();
        SearchHits hits = response.getHits();
        SearchHit[] resultHits = hits.getHits();
        int len = resultHits.length;
        for (int i = 0; i < len; i++) {
            SearchHit hit = resultHits[i];
            list.add(hit.getSourceAsMap());
        }
        return Optional.ofNullable(list);
    }
}
