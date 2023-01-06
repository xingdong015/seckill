package com.system.design.gateway.filter;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * @author chengzhengzheng
 * @date 2022/1/29
 */
@Component
public class LogGatewayFilterFactory extends AbstractGatewayFilterFactory<LogGatewayFilterFactory.Config> {

    public LogGatewayFilterFactory() {
        super(LogGatewayFilterFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("open");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                if (config.open) {
                    ServerHttpRequest request     = exchange.getRequest();
                    String            methodValue = request.getMethodValue();
                    URI               uri         = request.getURI();
                    RequestPath       path        = request.getPath();
                    HttpHeaders       headers     = request.getHeaders();
                    StringBuilder     sb          = new StringBuilder(methodValue);
                    sb.append("\n").append(uri).append("\n").append(path).append("\n").append(headers);
                    System.out.println(sb);
                }
                return chain.filter(exchange);
            }
        };
    }


    static class Config {
        private boolean open;

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }
    }
}
