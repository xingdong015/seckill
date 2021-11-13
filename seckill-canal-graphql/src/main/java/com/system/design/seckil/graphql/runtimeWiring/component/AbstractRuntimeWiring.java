package com.system.design.seckil.graphql.runtimeWiring.component;

import com.alibaba.fastjson.JSONObject;
import com.system.design.seckil.graphql.runtimeWiring.config.DataLoadInterface;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:37 下午
 * @description：抽象是现实类
 * @modified By：`
 * @version: 1.0
 */
public abstract class AbstractRuntimeWiring<T extends DataLoadInterface> implements CustomizerRuntimeWiring {

    @Resource
    private T reponse;

    public abstract String getFieldName();

    @Override
    public void loader(RuntimeWiring.Builder builder) {
        builder.type("query", item -> item.dataFetcher(getFieldName(), loaderFetcher()))
                .type("query",item -> item.dataFetcher(getFieldName(), loaderFetcherList()));
    }

    private DataFetcher<CompletionStage<Object>> loaderFetcher() {
        return env -> CompletableFuture.supplyAsync(() -> reponse.findOne(env));
    }
    private DataFetcher<CompletionStage<Object>> loaderFetcherList() {
        return env -> CompletableFuture.supplyAsync(() -> reponse.findList(env));
    }
}
