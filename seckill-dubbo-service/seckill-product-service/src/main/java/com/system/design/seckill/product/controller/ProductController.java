package com.system.design.seckill.product.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.system.design.seckill.common.dto.ProductDto;
import com.system.design.seckill.product.constant.EsIndexConstant;
import com.system.design.seckill.product.entity.MyResponse;
import com.system.design.seckill.product.utils.EsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 进行es检索入口
 * @author: 贾凯
 * @create: 2021-11-13 16:00
 */
@RestController
@RequestMapping("/es/product")
@CrossOrigin(allowCredentials="true")
@Slf4j
public class ProductController {
    @Resource
    private EsUtils esUtils;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    private static final String SUGGEST_NAME = "suggest_productName";

    /**
     *  @Description: 产品名称的条件输入框  自动补全，根据用户的输入联想到可能的词或者短语
     */
    @GetMapping("/search/{input}")
    public MyResponse inputSuggest(@PathVariable String input){
        try {
            // 1、创建search请求
            SearchRequest searchRequest = new SearchRequest(EsIndexConstant.getIndexName("t_product"));
            // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            //2.1做查询建议 //词项建议
            SuggestBuilder suggestBuilder = new SuggestBuilder();
            TermSuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion("product_name").text(input);
            suggestBuilder.addSuggestion(SUGGEST_NAME, termSuggestionBuilder);
            searchSourceBuilder.suggest(suggestBuilder);
            searchRequest.source(searchSourceBuilder);
            //3.发送请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //4.处理相应
            List sugggestList = new ArrayList();
            if (RestStatus.OK.equals(searchResponse.status())){
                //获取建议结果
                Suggest suggest = searchResponse.getSuggest();
                TermSuggestion termSuggestion = suggest.getSuggestion(SUGGEST_NAME);
                termSuggestion.getEntries().stream().forEach(entry -> {
                    log.info("前台输入---input: {}",entry.getText().toString());
                    entry.getOptions().stream().forEach(option -> {
                        sugggestList.add(option.getText().toString());
                        log.info("词项建议---suggest: {}", option.getText().toString());
                    });
                });
            }
            return MyResponse.success(sugggestList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MyResponse.error(Collections.emptyList());
    }

    
    /**
     * @Description: 多条件检索，按照创建时间倒序，高亮展示结果
    */
    @PostMapping("/search")
    public MyResponse searchComplex(@RequestBody ProductDto productDto){
        try {
            //1.创建searchrequest请求
            SearchRequest searchRequest = new SearchRequest(EsIndexConstant.getIndexName("t_product"));
            //2.用searchsourcebuilder构建查询请求体，所有条件都在这里封装
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //2.1检索条件
            BoolQueryBuilder boolQuery = getBoolQueryBuilder(productDto);
            //2.2列举返回的字段和不返回的字段
            ImmutableList<String> includes = ImmutableList.of("id","product_name","product_desc");
            ImmutableList<String> excludes = ImmutableList.of("create_time","update_time");
            //2.3高亮设置:红色显示产品名称
            HighlightBuilder.Field productNameField = new HighlightBuilder.Field("product_name").preTags("<span color='red'>").postTags("</span>");
            HighlightBuilder.Field productDescField = new HighlightBuilder.Field("product_desc").preTags("<strong>").postTags("/strong");
            HighlightBuilder highlightBuilder = new HighlightBuilder().field(productNameField).field(productDescField);
            //2.4填充所有条件
            searchSourceBuilder
                    .query(boolQuery)
                    .size(productDto.getPageSize().intValue())
                    .from(productDto.getPageSize().intValue())
                    .sort("create_time", SortOrder.DESC)
                    .fetchSource(includes.toArray(new String[0]),excludes.toArray(new String[0]))
                    .highlighter(highlightBuilder);
            searchRequest.source(searchSourceBuilder);
            //3.发送请求
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //4.处理响应
            if (RestStatus.OK.equals(response.status())){
                SearchHits hits = response.getHits();
                //4.1获取分页信息：命中的总数据条数，总页数
                long totalCount = hits.getTotalHits().value;
                int totalPage = (int) Math.ceil(totalCount / productDto.getPageSize().intValue());
                //4.2处理搜索命中的文档：取高亮结果进行SourceAsMap回填
                SearchHit[] searchHits = hits.getHits();
                for (SearchHit searchHit : searchHits) {
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    log.info("====== 检索结果：{}", JSON.toJSONString(sourceAsMap));
                    createHightlightContent(sourceAsMap, searchHit.getHighlightFields(), "product_name");
                    createHightlightContent(sourceAsMap, searchHit.getHighlightFields(), "product_desc");
                }

                //5.组装返回值
                HashMap<String, Object> resMap = new HashMap<>();
                resMap.put("totalCount", totalCount);
                resMap.put("totalPage", totalPage);
                resMap.put("data", Arrays.stream(searchHits).map(SearchHit::getSourceAsMap).collect(Collectors.toList()));
                return MyResponse.success(resMap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return MyResponse.error(Collections.emptyList());
    }


    /**
     * @Description:  聚合统计查询
    */
    @PostMapping("/aggregation/{productName}")
    public MyResponse aggregation(@PathVariable String productName){
        try {
            //1.创建search请求
            SearchRequest searchRequest = new SearchRequest(EsIndexConstant.getIndexName("t_product"));
            //2.构建searchsourcebuilder查询体，所有查询以及聚合条件都封装在这里
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);
            //2.1加入聚合：按照产品名称分组聚合，内嵌每组价格平均值
            TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("agg_productName").field("product_name").order(BucketOrder.aggregation("agg_avgPrice", true));
            aggregationBuilder.subAggregation(AggregationBuilders.avg("agg_avgPrice").field("price"));
            searchSourceBuilder.aggregation(aggregationBuilder);
            searchRequest.source(searchSourceBuilder);
            //3.发送请求
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //4.处理响应
            if (RestStatus.OK.equals(response.status())){
                //获取聚合结果
                Aggregations aggregations = response.getAggregations();
                Terms productNameAgg = aggregations.get("agg_productName");
                log.info("aggregation agg_productName 结果");
                log.info("docCountError: " + productNameAgg.getDocCountError());
                log.info("sumOfOtherDocCounts: " + productNameAgg.getSumOfOtherDocCounts());
                log.info("------------------------------------");
                productNameAgg.getBuckets().stream().forEach(bucket->{
                    log.info("key: " + bucket.getKeyAsNumber());
                    log.info("docCount: " + bucket.getDocCount());
                    log.info("docCountError: " + bucket.getDocCountError());
                    //取子聚合
                    Avg aggAvgPrice = bucket.getAggregations().get("agg_avgPrice");
                    log.info("average_balance: " + aggAvgPrice.getValue());
                    log.info("------------------------------------");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return MyResponse.success(Collections.emptyList());
    }


    //封装检索条件
    private BoolQueryBuilder getBoolQueryBuilder(@RequestBody ProductDto productDto) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (productDto != null){
            if (StringUtils.isNotEmpty(productDto.getKeyword())){
                //boolQuery.should(QueryBuilders.multiMatchQuery(productDto.getKeyword(),"product_name","product_desc"));
                //返回包含与搜索词相似的词的文档
                boolQuery.should(QueryBuilders.fuzzyQuery("product_name",productDto.getProductName()));
                boolQuery.should(QueryBuilders.fuzzyQuery("product_desc",productDto.getProductDesc()));
            }
            if (StringUtils.isNotEmpty(productDto.getProductName())){
                //查询某个字段中模糊包含目标字符串，使用matchQuery：match查询会先对搜索词进行分词,分词完毕后再逐个对分词结果进行匹配
                boolQuery.must(QueryBuilders.matchQuery("product_name",productDto.getProductName()));
            }
            if (StringUtils.isNotEmpty(productDto.getProductDesc())){
                //match_phrase查询首先解析查询字符串来产生一个词条列表。然后会搜索所有的词条，但只保留包含了所有搜索词条的文档，并且词条的位置要邻接;
                //slop让词之间可以间隔容错
                boolQuery.must(QueryBuilders.matchPhraseQuery("product_desc",productDto.getProductDesc()).slop(2));
            }
            if (productDto.getMinPrice().intValue() > 0 || productDto.getMaxPrice().intValue() > 0) {
                RangeQueryBuilder priceRangeQuery = QueryBuilders.rangeQuery("price");
                if (productDto.getMinPrice().intValue() > 0) {
                    priceRangeQuery.gte(productDto.getMinPrice());
                }
                if (productDto.getMaxPrice().intValue() > 0) {
                    priceRangeQuery.lte(productDto.getMaxPrice());
                }
                boolQuery.must(priceRangeQuery);
            }
            if (productDto.getCreateTimeStart().intValue() > 0 || productDto.getCreateTimeEnd().intValue() > 0) {
                //startTime和endTime都是Unix时间戳（秒）:QueryBuilders.rangeQuery("time").format("epoch_second").gte(startTime).lte(endTime);
                //startTime和endTime都是Unix时间戳（秒）:QueryBuilders.rangeQuery("time").format("epoch_millis").gte(startTime * 1000).lte(endTime * 1000);
                RangeQueryBuilder createTimeRangeQuery = QueryBuilders.rangeQuery("create_time").format("epoch_millis");
                if (productDto.getCreateTimeStart().intValue() > 0) {
                    createTimeRangeQuery.gte(productDto.getCreateTimeStart()*1000);
                }
                if (productDto.getCreateTimeEnd().intValue() > 0) {
                    createTimeRangeQuery.lte(productDto.getCreateTimeEnd()*1000);
                }
                boolQuery.must(createTimeRangeQuery);
            }
        }
        return boolQuery;
    }

    //回填，替换为高亮显示字段的内容
    private void createHightlightContent(Map<String, Object> sourceAsMap, Map<String, HighlightField> highlightFields, String field) {
        HighlightField productNameHighlight = highlightFields.get(field);
        if (productNameHighlight != null){
            Text[] fragments = productNameHighlight.fragments();
            StringBuilder stringBuilder = new StringBuilder();
            for (Text text : fragments) {
                stringBuilder.append(text.toString());
            }
            sourceAsMap.put(field,stringBuilder.toString());
        }
    }



}
