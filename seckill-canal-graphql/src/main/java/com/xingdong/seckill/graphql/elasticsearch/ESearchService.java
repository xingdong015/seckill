package com.xingdong.seckill.graphql.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.Optional;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/16 1:48 下午
 * @description：查询接口
 * @modified By：`
 * @version: 1.0
 */
public interface ESearchService {

    Optional<Object> searchSimple(String tableName, QueryBuilder queryBuilder, Integer page, Integer size) throws Exception;
}
