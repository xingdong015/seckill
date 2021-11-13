package com.system.design.seckil.graphql.runtimeWiring.config;

import com.alibaba.fastjson.JSONObject;
import graphql.schema.DataFetchingEnvironment;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public interface DataLoadInterface {

    <U> U findOne(DataFetchingEnvironment env);

    <U> U findList(DataFetchingEnvironment env);
}
