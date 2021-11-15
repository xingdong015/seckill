package com.system.design.seckil.graphql.runtimeWiring.response;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public interface ServiceInterface {

    Object findOne(DataFetchingEnvironment env);

    List findList(DataFetchingEnvironment env);
}
