package com.system.design.seckill.product.utils;

import com.alibaba.fastjson.JSON;
import com.system.design.seckill.product.config.ESClientConfig;
import com.system.design.seckill.product.entity.ElasticEntity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-03-18 15:11
 */
@Slf4j
public class EsUtils {

    private static final int TIMEOUT = 10;

    //创建索引
    public static CreateIndexResponse createIndex(RestHighLevelClient restHighLevelClient, String indexName) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    //判断索引是否存在
    public static boolean existIndex(RestHighLevelClient restHighLevelClient, String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    //删除索引
    public static boolean deleteIndex(RestHighLevelClient restHighLevelClient, String indexName) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        return delete.isAcknowledged();
    }

    public static void createIndex(RestHighLevelClient restHighLevelClient, String idxName,String idxSQL){
        try {
            if (existIndex(restHighLevelClient, idxName)) {
                log.error(" idxName={} 已经存在,idxSql={}",idxName,idxSQL);
                return;
            }
            // 1.创建索引的请求
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            ESClientConfig.buildSetting(request);
            //2.// 创建映射
            if(!StringUtils.isEmpty(idxSQL)) {
                request.mapping(idxSQL, XContentType.JSON);
            }
            //request.settings() 手工指定Setting
            //3.执行创建操作并得到响应
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void insertOrUpdateOne(RestHighLevelClient restHighLevelClient, String idxName, ElasticEntity entity) {
        IndexRequest request = new IndexRequest(idxName);
        request.id(entity.getId());
        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        try {
            IndexResponse index = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            log.info("-->{}", index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量插入数据
     */
    public static void insertBatch(RestHighLevelClient restHighLevelClient, String idxName, List<ElasticEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(idxName).id(item.getId())
                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量删除
     */
    public static <T> void deleteBatch(RestHighLevelClient restHighLevelClient, String idxName, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(idxName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //分页检索
    public static Optional<Object> searchSimple(RestHighLevelClient restHighLevelClient, String tableName, QueryBuilder queryBuilder, Integer page, Integer size) throws Exception {
        //todo 抽象 包装一个统一的response
        SearchRequest searchRequest = new SearchRequest(tableName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().trackTotalHits(true);
        searchSourceBuilder.query(queryBuilder)
                .size(size).from(page * size + 1)
                .timeout(new TimeValue(TIMEOUT, TimeUnit.SECONDS));
//        Scroll scroll = new Scroll(TimeValue.timeValueSeconds(10));
        SearchRequest source = searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(source, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] resultHits = hits.getHits();
        int len = resultHits.length;
        return len <= 0 ? Optional.empty() :
                Optional.of(Arrays.stream(resultHits).map(SearchHit::getSourceAsMap).collect(Collectors.toList()));
    }

    //分页检索
    public static Map search(RestHighLevelClient restHighLevelClient, String tableName, QueryBuilder queryBuilder, Integer page, Integer size, String sortField, String... highlightFields) throws Exception {
        //todo 抽象 包装一个统一的response
        SearchRequest searchRequest = new SearchRequest(tableName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().trackTotalHits(true);
        searchSourceBuilder.query(queryBuilder)
                .size(size).from(page * size + 1)
                .sort(sortField, SortOrder.DESC)
                .timeout(new TimeValue(TIMEOUT, TimeUnit.SECONDS));
        SearchRequest source = searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(source, RequestOptions.DEFAULT);

        //============计算总数量
        long totalCount = response.getHits().getTotalHits().value;
        //============得到总页数:Math.ceil()“向上取整”
        int totalPage = (int) Math.ceil((float) totalCount / size);

        //============处理检索结果，高亮展示
        SearchHits hits = response.getHits();
        SearchHit[] resultHits = hits.getHits();
        List<Map<String, Object>> data = Arrays.stream(resultHits).map(SearchHit::getSourceAsMap).collect(Collectors.toList());

        //整理返回对象
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("totalCount",totalCount);
        resMap.put("totalPage",totalPage);
        resMap.put("data",data);

        return resMap;
    }


    //默认每次抓取10条
    public static <T> List<T> search(RestHighLevelClient restHighLevelClient, String idxName, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void deleteByQuery(RestHighLevelClient restHighLevelClient, String idxName, QueryBuilder builder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getClazz(String clazzName){
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    /**
     * @date 2019/10/26 0:01
     * @param queryBuilder  设置查询对象
     * @param from  设置from选项，确定要开始搜索的结果索引。 默认为0。
     * @param size  设置大小选项，确定要返回的搜索匹配数。 默认为10。
     * @param timeout
     */
    public static SearchSourceBuilder initSearchSourceBuilder(QueryBuilder queryBuilder, int from, int size, int timeout){

        //使用默认选项创建 SearchSourceBuilder 。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //设置查询对象。可以使任何类型的 QueryBuilder
        sourceBuilder.query(queryBuilder);
        //设置from选项，确定要开始搜索的结果索引。 默认为0。
        sourceBuilder.from(from);
        //设置大小选项，确定要返回的搜索匹配数。 默认为10。
        sourceBuilder.size(size);
        sourceBuilder.timeout(new TimeValue(timeout, TimeUnit.SECONDS));
        return sourceBuilder;
    }

    public static SearchSourceBuilder initSearchSourceBuilder(QueryBuilder queryBuilder){
        return initSearchSourceBuilder(queryBuilder,0,10,60);
    }
}
