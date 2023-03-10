package com.xingdong.seckill.graphql.elasticsearch;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/16 1:48 下午
 * @description：查询接口
 * @modified By：`
 * @version: 1.0
 */
@Component
public class ESearchServiceImpl implements ESearchService {

    @Resource
    RestHighLevelClient restHighLevelClient;

    @Override
    public Optional<Object> searchSimple(String tableName, QueryBuilder queryBuilder, Integer page, Integer size) throws Exception {
        //todo 抽象 包装一个统一的response
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(tableName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().trackTotalHits(true);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(size);
        searchSourceBuilder.from(page * size + 1);
//        Scroll scroll = new Scroll(TimeValue.timeValueSeconds(10));
        SearchRequest source = searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(source, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] resultHits = hits.getHits();
        int len = resultHits.length;
        return len <= 0 ? Optional.empty() :
                Optional.of(Arrays.stream(resultHits).map(SearchHit::getSourceAsMap).collect(Collectors.toList()));
    }
}
