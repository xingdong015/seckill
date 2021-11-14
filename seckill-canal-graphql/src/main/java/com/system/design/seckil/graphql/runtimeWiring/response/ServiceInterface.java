package com.system.design.seckil.graphql.runtimeWiring.response;

import graphql.schema.DataFetchingEnvironment;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public interface ServiceInterface {

    <U> U findOne(DataFetchingEnvironment env);

    <U> U findList(DataFetchingEnvironment env);
}
