package com.system.design.gateway.handler;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class SeckillErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    public SeckillErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                           ResourceProperties resourceProperties,
                                           ErrorProperties errorProperties,
                                           ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        boolean             includeStackTrace = isIncludeStackTrace(request, MediaType.ALL);
        Map<String, Object> errorMap          = getErrorAttributes(request, includeStackTrace);
        int                 status            = Integer.valueOf(errorMap.get("status").toString());
        Map<String, Object> response          = response(status, errorMap.get("error").toString(), errorMap);
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(response));
    }

    // 我们希望返回的数据结构
    public static Map<String, Object> response(int status, String errorMessage, Map<String, Object> errorMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", status);
        map.put("message", errorMessage);
        map.put("data", errorMap);
        return map;
    }
}