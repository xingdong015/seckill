package com.xingdong.seckill.graphql.component;

import com.xingdong.seckill.graphql.utils.ThreadUtils;
import com.xingdong.seckill.graphql.service.IService;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ：程征波
 * @date ：Created in 2021/11/12 4:37 下午
 * @description：抽象是现实类
 * @modified By：`
 * @version: 1.0
 */
public abstract class AbstractRuntimeWiring<T extends IService> implements CustomizerRuntimeWiring {

    private static final ThreadPoolExecutor executor = ThreadUtils.getThread(" ###AbstractRuntimeWiring### ");

    @Autowired
    private T seviceImpl;

    public abstract String getMethodName();

    @Override
    public void loader(@Autowired RuntimeWiring.Builder builder) {
        builder.type("Query", item -> item.dataFetcher(getMethodName(), loaderFetcher()))
                .type("Query", item -> item.dataFetcher(getMethodName() + "List", loaderFetcherList()))
                .type("Query", item -> item.dataFetcher(getMethodName() + "Page", loaderFetcherPage()));
    }

    public DataFetcher<CompletionStage<Object>> loaderFetcher() {
        return env -> CompletableFuture.supplyAsync(() -> seviceImpl.findOne(env), executor);
    }

    private DataFetcher<CompletionStage<Object>> loaderFetcherList() {
        return env -> CompletableFuture.supplyAsync(() -> seviceImpl.findList(env), executor);
    }

    private DataFetcher<CompletionStage<Object>> loaderFetcherPage() {
        return env -> CompletableFuture.supplyAsync(() -> seviceImpl.findList(env), executor);
    }
}
