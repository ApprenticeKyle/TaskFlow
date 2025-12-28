package org.r2learning.gateway.infrastructure.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service", r -> r
                .path("/auth/**")
                .filters(f -> f
                    .stripPrefix(1)
                    .addRequestHeader("X-Request-Source", "gateway")
                    .circuitBreaker(config -> config
                        .setName("authService")
                        .setFallbackUri("forward:/fallback/auth"))
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(apiKeyResolver()))
                )
                .uri("lb://auth-service"))

            .route("project-service", r -> r
                .path("/api/projects/**")
                .filters(f -> f
                    .rewritePath("/api/projects/(?<segment>.*)", "/${segment}")
                    .addRequestHeader("X-Request-Source", "gateway")
                    .circuitBreaker(config -> config
                        .setName("projectService")
                        .setFallbackUri("forward:/fallback/project"))
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(apiKeyResolver()))
                )
                .uri("lb://project-service"))

            .route("task-service", r -> r
                .path("/api/tasks/**")
                .filters(f -> f
                    .rewritePath("/api/tasks/(?<segment>.*)", "/${segment}")
                    .addRequestHeader("X-Request-Source", "gateway")
                    .circuitBreaker(config -> config
                        .setName("taskService")
                        .setFallbackUri("forward:/fallback/task"))
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(apiKeyResolver()))
                )
                .uri("lb://task-service"))

            .route("notification-service", r -> r
                .path("/api/notifications/**")
                .filters(f -> f
                    .rewritePath("/api/notifications/(?<segment>.*)", "/${segment}")
                    .addRequestHeader("X-Request-Source", "gateway")
                    .circuitBreaker(config -> config
                        .setName("notificationService")
                        .setFallbackUri("forward:/fallback/notification"))
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(apiKeyResolver()))
                )
                .uri("lb://notification-service"))

            .route("analytics-service", r -> r
                .path("/api/analytics/**")
                .filters(f -> f
                    .rewritePath("/api/analytics/(?<segment>.*)", "/${segment}")
                    .addRequestHeader("X-Request-Source", "gateway")
                    .circuitBreaker(config -> config
                        .setName("analyticsService")
                        .setFallbackUri("forward:/fallback/analytics"))
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(apiKeyResolver()))
                )
                .uri("lb://analytics-service"))

            .route("search-service", r -> r
                .path("/api/search/**")
                .filters(f -> f
                    .rewritePath("/api/search/(?<segment>.*)", "/${segment}")
                    .addRequestHeader("X-Request-Source", "gateway")
                    .circuitBreaker(config -> config
                        .setName("searchService")
                        .setFallbackUri("forward:/fallback/search"))
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(apiKeyResolver()))
                )
                .uri("lb://search-service"))

            .route("file-service", r -> r
                .path("/api/files/**")
                .filters(f -> f
                    .rewritePath("/api/files/(?<segment>.*)", "/${segment}")
                    .addRequestHeader("X-Request-Source", "gateway")
                    .circuitBreaker(config -> config
                        .setName("fileService")
                        .setFallbackUri("forward:/fallback/file"))
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())
                        .setKeyResolver(apiKeyResolver()))
                )
                .uri("lb://file-service"))
            .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20, 1);
    }

    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> {
            String apiKey = exchange.getRequest()
                .getHeaders()
                .getFirst("X-API-KEY");

            if (StringUtils.hasText(apiKey)) {
                return Mono.just(apiKey);
            }

            apiKey = exchange.getRequest()
                .getQueryParams()
                .getFirst("apiKey");

            if (StringUtils.hasText(apiKey)) {
                return Mono.just(apiKey);
            }

            return Mono.just(exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress());
        };
    }

}
