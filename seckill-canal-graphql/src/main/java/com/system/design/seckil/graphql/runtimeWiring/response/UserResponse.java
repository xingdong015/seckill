package com.system.design.seckil.graphql.runtimeWiring.response;

import com.alibaba.fastjson.JSONObject;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 程征波
 * @date 2021/11/13
 */
@Repository
public class UserResponse implements ServiceInterface {

    @Override
    public Object findOne(DataFetchingEnvironment env) {
        //// TODO: 2021/11/13
        String     query      = env.getArgument("query");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);
        return null;
    }

    @Override
    public List findList(DataFetchingEnvironment env) {
        //// TODO: 2021/11/13
        String     query      = env.getArgument("query");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);
        return null;
    }
}
