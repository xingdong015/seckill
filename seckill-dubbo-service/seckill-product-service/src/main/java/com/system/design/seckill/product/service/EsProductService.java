package com.system.design.seckill.product.service;

import com.google.common.collect.ImmutableList;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-12-09 10:44
 */
public interface EsProductService {
    SearchRequest createSuggestSearchRequest(String input, String tableName, String fieldName, SuggestBuilder suggestBuilder);
    List doSuggestRequest(SearchRequest searchRequest,  String suggestName) throws IOException;

    SearchResponse getSearchResponse(String indexName, BoolQueryBuilder boolQuery, ImmutableList<String> includes, ImmutableList<String> excludes, HighlightBuilder highlightBuilder, Integer size, Integer from, LinkedHashMap<String, SortOrder> sortOrderMap) throws IOException;
    HashMap<String, Object> createResultMap(SearchResponse response, List<String> replaceHightlightFields, Integer size);

    void replaceHightlightContent(SearchHit searchHit, String... fields);
}
