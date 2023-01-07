package com.xingdong.seckill.product.service.impl;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.ImmutableList;
import com.xingdong.seckill.product.constant.EsIndexConstant;
import com.xingdong.seckill.product.service.EsProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-12-09 10:45
 */
@Service
@Slf4j
public class EsProductServiceImpl implements EsProductService {
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchRequest createSuggestSearchRequest(String input, String tableName, String fieldName, SuggestBuilder suggestBuilder) {
        SearchRequest searchRequest = new SearchRequest(EsIndexConstant.getIndexName(tableName));
        // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        //2.1做查询建议 //词项建议
        searchSourceBuilder.suggest(suggestBuilder);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    @Override
    public List doSuggestRequest(SearchRequest searchRequest,  String suggestName) throws IOException {
        //3.发送请求
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //4.处理相应
        List sugggestList = new ArrayList();
        if (RestStatus.OK.equals(searchResponse.status())){
            //获取建议结果
            Suggest suggest = searchResponse.getSuggest();
            TermSuggestion termSuggestion = suggest.getSuggestion(suggestName);
            termSuggestion.getEntries().stream().forEach(entry -> {
                log.info("前台输入---input: {}",entry.getText().toString());
                entry.getOptions().stream().forEach(option -> {
                    sugggestList.add(option.getText().toString());
                    log.info("词项建议---suggest: {}", option.getText().toString());
                });
            });
        }
        return sugggestList;
    }

    @Override
    public SearchResponse getSearchResponse(String indexName, BoolQueryBuilder boolQuery, ImmutableList<String> includes, ImmutableList<String> excludes, HighlightBuilder highlightBuilder, Integer size, Integer from, LinkedHashMap<String, SortOrder> sortOrderMap) throws IOException {
        //1.创建searchrequest请求
        SearchRequest searchRequest = new SearchRequest(indexName);
        //2.用searchsourcebuilder构建查询请求体，所有条件都在这里封装
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //2.4填充所有条件
        if (boolQuery != null){searchSourceBuilder.query(boolQuery);}
        if (CollectionUtils.isNotEmpty(includes) && CollectionUtils.isNotEmpty(excludes)){searchSourceBuilder.fetchSource(includes.toArray(new String[0]), excludes.toArray(new String[0]));}
        if (highlightBuilder != null){searchSourceBuilder.highlighter(highlightBuilder);}
        if (from != null){searchSourceBuilder.from(from);}
        if (size != null){searchSourceBuilder.size(size);}
        if (!MapUtil.isEmpty((sortOrderMap))){
            sortOrderMap.forEach((key, value)->{
                searchSourceBuilder.sort(key,value);
            });
        }
        searchRequest.source(searchSourceBuilder);
        //3.发送请求
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public HashMap<String, Object> createResultMap(SearchResponse response, List<String> replaceHightlightFields, Integer size) {
        HashMap<String, Object> resMap = new HashMap<>();
        if (RestStatus.OK.equals(response.status())){
            SearchHits hits = response.getHits();
            if (CollectionUtils.isNotEmpty(replaceHightlightFields)) {
                //处理搜索命中的文档：取高亮结果进行SourceAsMap回填
                for (SearchHit searchHit : hits.getHits()) {
                    replaceHightlightContent(searchHit, replaceHightlightFields.toArray(new String[0]));
                }
            }
            //组装返回值
            resMap.put("totalCount", hits.getTotalHits().value);
            if (size != null && size > 0) {
                resMap.put("totalPage", (int) Math.ceil(hits.getTotalHits().value / size));
            }
            resMap.put("data", Arrays.stream(hits.getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList()));
        }
        return resMap;
    }

    /**
     * @Description: 回填，替换为高亮显示字段的内容
     * @author jiakai
     * @date 2021/12/15 13:52
    */
    @Override
    public void replaceHightlightContent(SearchHit searchHit, String... fields) {
        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
        Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
        Arrays.stream(fields).forEach(field->{
            HighlightField highlightField = highlightFields.get(field);
            if (highlightField != null){
                Text[] fragments = highlightField.getFragments();
                StringBuilder stringBuilder = new StringBuilder();
                for (Text fragment : fragments) {
                    stringBuilder.append(fragment);
                }
                sourceAsMap.put(field, stringBuilder.toString());
            }
        });
    }
}
