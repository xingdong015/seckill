package com.system.design.seckil.graphql.runtimeWiring;

import com.alibaba.fastjson.JSONObject;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:37 下午
 * @description：抽象是现实类
 * @modified By：`
 * @version: 1.0
 */
public abstract class AbstractCustomizerRuntimeWiring implements CustomizerRuntimeWiring {

    public String fieldName = "t_product";

    @Override
    public void loader(RuntimeWiring.Builder builder) {
        builder.type("query", item -> item.dataFetcher(fieldName, loaderFetcher()));
    }

    private DataFetcher<CompletionStage<Object>> loaderFetcher() {
        return env -> {
            String query = env.getArgument("query");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",query);
            return CompletableFuture.supplyAsync(jsonObject::toJSONString);
        };
    }
}
