package com.system.design.seckill.product.es;

import com.alibaba.fastjson.JSON;
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
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-03-18 15:11
 */
@Slf4j
@Component
public class EsOptionUtil {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    public void createIndex(String idxName,String idxSQL){
        try {

            if (this.indexExist(idxName)) {
                log.error(" idxName={} 已经存在,idxSql={}",idxName,idxSQL);
                return;
            }
            // 1.创建索引的请求
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSetting(request);
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

    /** 断某个index是否存在
     */
    public boolean indexExist(String idxName) throws Exception {
        GetIndexRequest request = new GetIndexRequest(idxName);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    /** 设置分片
     */
    public void buildSetting(CreateIndexRequest request){

        request.settings(Settings.builder().put("index.number_of_shards",1)
                .put("index.number_of_replicas",1));
    }

    public void insertOrUpdateOne(String idxName, ElasticEntity entity) {
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
    public void insertBatch(String idxName, List<ElasticEntity> list) {
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
    public <T> void deleteBatch(String idxName, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(idxName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public <T> List<T> search(String idxName, SearchSourceBuilder builder, Class<T> c) {
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

    public <T> Map<String,Object> searchMap(String idxName, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            //此处是我写的，需要验证是否表示检索总条数
            long value = response.getHits().getTotalHits().value;

            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("data",res);
            map.put("count",value);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 删除index
     */
    public void deleteIndex(String idxName) {
        try {
            if (!this.indexExist(idxName)) {
                log.error(" idxName={} 已经存在",idxName);
                return;
            }
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
            // 得到响应
            boolean acknowledged = acknowledgedResponse.isAcknowledged();
            System.out.println(acknowledged);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteByQuery(String idxName, QueryBuilder builder) {
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
