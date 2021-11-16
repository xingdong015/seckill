package com.system.design.seckil.graphql.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.system.design.seckil.graphql.elasticsearch.ESearchService;
import com.system.design.seckil.graphql.elasticsearch.ESearchServiceImpl;
import graphql.schema.DataFetchingEnvironment;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public abstract class IAbstractService implements IService{

    @Autowired
    ESearchServiceImpl eSearchService;

    public abstract String getIndex();

    @Override
    public Object findOne(DataFetchingEnvironment env) {
        // TODO: 2021/11/13
        String query = env.getArgument("query");
        try {
            JSONObject jsonObject = JSONObject.parseObject("{bool: {must: [{match_all: {}}], must_not: [], should: []}}");
            QueryBuilder queryBuilder = QueryBuilders.wrapperQuery(jsonObject.toJSONString());
            Optional<Object> objectOptional = eSearchService.searchSimple(getIndex(), queryBuilder);
            if(objectOptional.isPresent()) {
                List list = (List)objectOptional.get();
                return list.get(0);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return env;
    }

    @Override
    public List findList(DataFetchingEnvironment env) {
        // TODO: 2021/11/13
        String query = env.getArgument("query");
        try {
            JSONArray array = JSONObject.parseArray("{bool: {must: [{match_all: {}}], must_not: [], should: []}}");
            QueryBuilder queryBuilder = QueryBuilders.wrapperQuery(array.toJSONString());
            Optional<Object> objectOptional = eSearchService.searchSimple(getIndex(),  queryBuilder);
            if(objectOptional.isPresent()) {
                return  (List)objectOptional.get();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.singletonList(env);
    }
}
