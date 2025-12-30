package org.r2learning.gateway.infrastructure.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashSet;

@Component
@Slf4j
public class RouteDebugFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String method = request.getMethod().name();

        log.info("========================================");
        log.info("🚀 请求进入 Gateway");
        log.info("========================================");
        log.info("请求方法: {}", method);
        log.info("请求路径: {}", path);
        log.info("完整URL: {}", request.getURI());
        log.info("远程地址: {}", request.getRemoteAddress() != null 
            ? request.getRemoteAddress().getAddress().getHostAddress() : "unknown");
        log.info("请求头: {}", request.getHeaders().toSingleValueMap());
        log.info("查询参数: {}", request.getQueryParams().toSingleValueMap());

        return chain.filter(exchange).doOnSuccess(aVoid -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            if (route != null) {
                log.info("========================================");
                log.info("🎯 路由分发信息");
                log.info("========================================");
                log.info("匹配的路由ID: {}", route.getId());
                log.info("路由URI: {}", route.getUri());
                log.info("路由断言: {}", route.getPredicate());
                log.info("路由过滤器: {}", route.getFilters());

                URI targetUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                if (targetUri != null) {
                    log.info("目标服务URI: {}", targetUri);
                }

                LinkedHashSet<URI> originalUris = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
                if (originalUris != null && !originalUris.isEmpty()) {
                    URI originalUri = originalUris.iterator().next();
                    log.info("原始请求URI: {}", originalUri);
                }

                log.info("========================================");
            } else {
                log.warn("⚠️  未匹配到任何路由: {}", path);
            }
        }).doOnError(throwable -> {
            log.error("❌ 请求处理失败: {}", path, throwable);
        });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}