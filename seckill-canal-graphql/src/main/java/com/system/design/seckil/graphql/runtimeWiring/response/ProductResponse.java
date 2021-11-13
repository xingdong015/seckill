package com.system.design.seckil.graphql.runtimeWiring.response;

import com.alibaba.fastjson.JSONObject;
import com.system.design.seckil.graphql.runtimeWiring.config.DataLoadInterface;
import graphql.schema.DataFetchingEnvironment;

/**
 * @author 程征波
 * @date 2021/11/13
 */
public class ProductResponse implements DataLoadInterface {

    @Override
    public <U> U findOne(DataFetchingEnvironment env) {
        //// TODO: 2021/11/13
        String     query      = env.getArgument("query");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);
        return null;
    }

    @Override
    public <U> U findList(DataFetchingEnvironment env) {
        //// TODO: 2021/11/13
        String     query      = env.getArgument("query");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);
        return null;
    }
}
