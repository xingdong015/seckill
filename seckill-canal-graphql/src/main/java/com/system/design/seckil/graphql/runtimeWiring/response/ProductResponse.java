package com.system.design.seckil.graphql.runtimeWiring.response;

import com.alibaba.fastjson.JSONObject;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Repository;

/**
 * @author 程征波
 * @date 2021/11/13
 */
@Repository
public class ProductResponse implements ServiceInterface {

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
