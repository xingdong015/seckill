package com.system.design.seckil.graphql.runtimeWiring.service;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public interface IService {

    Object findOne(DataFetchingEnvironment env);

    List findList(DataFetchingEnvironment env);
}
