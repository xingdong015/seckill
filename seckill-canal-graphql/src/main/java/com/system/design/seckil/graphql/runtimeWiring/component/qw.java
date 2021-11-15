//package com.system.design.seckil.graphql.runtimeWiring.component;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class GraphqlWebInterceptor implements Interceptor {
//
//    @Autowired
//    DataLoaderRegisterFactory dataLoaderRegisterFactory;
//
//    @Override
//    public Mono<WebOutput> intercept(WebInput webInput, WebGraphQlHandler webGraphQlHandler) {
//        webInput.configureExecutionInput(((executionInput, builder) -> {
//            return builder.dataLoaderRegistry(dataLoaderRegisterFactory.create()).build();
//        }));
//        return webGraphQlHandler.handle(webInput);
//    }
//}