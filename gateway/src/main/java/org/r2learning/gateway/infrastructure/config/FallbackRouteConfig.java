package org.r2learning.gateway.infrastructure.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.r2learning.gateway.handler.FallbackHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FallbackRouteConfig {

    private final FallbackHandler fallbackHandler;

    public FallbackRouteConfig(FallbackHandler fallbackHandler) {
        this.fallbackHandler = fallbackHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoutes() {
        return route(path("/fallback/**"), fallbackHandler::fallback);
    }
}