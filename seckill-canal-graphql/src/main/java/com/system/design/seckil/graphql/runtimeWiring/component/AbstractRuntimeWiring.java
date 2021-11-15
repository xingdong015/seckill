package com.system.design.seckil.graphql.runtimeWiring.component;

import com.system.design.seckil.graphql.runtimeWiring.response.ServiceInterface;
import com.system.design.seckil.graphql.runtimeWiring.utils.ThreadUtils;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:37 下午
 * @description：抽象是现实类
 * @modified By：`
 * @version: 1.0
 */
public abstract class AbstractRuntimeWiring<T extends ServiceInterface> implements CustomizerRuntimeWiring {

    @Autowired
    private T reponse;

    public abstract String getMethodName();

    @Override
    public void loader(@Autowired RuntimeWiring.Builder builder) {
        builder.type("Query", item -> item.dataFetcher(getMethodName(), loaderFetcher()))
                .type("Query", item -> item.dataFetcher(getMethodName() + "List", loaderFetcherList()));
    }

    public DataFetcher<CompletionStage<Object>> loaderFetcher() {
        return env -> CompletableFuture.supplyAsync(() -> reponse.findOne(env), ThreadUtils.getThread(getMethodName()));
    }

    private DataFetcher<CompletionStage<Object>> loaderFetcherList() {
        return env -> CompletableFuture.supplyAsync(() -> reponse.findList(env), ThreadUtils.getThread(getMethodName()));
    }
}
