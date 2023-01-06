package com.system.design.seckil.graphql.service;

import com.system.design.seckil.graphql.elasticsearch.ESearchServiceImpl;
import com.system.design.seckil.graphql.elasticsearch.QueryDataBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public abstract class IAbstractService implements IService {

    @Autowired
    ESearchServiceImpl eSearchService;

    public abstract String getIndex();

    @Override
    public Object findOne(DataFetchingEnvironment env) {
        // TODO: 2021/11/13
        try {
            String query = env.getArgument("query");
            QueryBuilder queryBuilder = QueryDataBuilder.builder(query);
            Optional<Object> objectOptional = eSearchService.searchSimple(getIndex(), queryBuilder, 0, 1);
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
        try {
            String query = env.getArgument("query");
            QueryBuilder queryBuilder = QueryDataBuilder.builder(query);
            Optional<Object> objectOptional = eSearchService.searchSimple(getIndex(), queryBuilder, 0, 10);
            if(objectOptional.isPresent()) {
                return (List)objectOptional.get();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.singletonList(env);
    }

    @Override
    public Page findPage(DataFetchingEnvironment env) {
        try {
            String query = env.getArgument("query");
            Integer size = env.getArgument("size");
            Integer page = env.getArgument("page");
            QueryBuilder queryBuilder = QueryDataBuilder.builder(query);
            Optional<Object> objectOptional = eSearchService.searchSimple(getIndex(), queryBuilder, page, size);
            if(objectOptional.isPresent()) {
                return new PageImpl((List)objectOptional.get());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new PageImpl(Collections.emptyList());
    }
}
