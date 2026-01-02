package org.r2learning.gateway.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtDecoder jwtDecoder;
    private final ObjectMapper objectMapper;
    private final List<String> skipPaths;

    public AuthenticationFilter(JwtDecoder jwtDecoder, ObjectMapper objectMapper) {
        this.jwtDecoder = jwtDecoder;
        this.objectMapper = objectMapper;
        this.skipPaths = List.of("/api/auth/", 
            "/auth/",
            "/actuator/health",
            "/actuator/info",
            "/favicon.ico",
            "/webjars/",
            "/v3/api-docs",
            "/swagger-ui/",
            "/swagger-resources/",
            "/swagger-ui.html",
            "/csrf",
            "/fallback/"  // 添加fallback路径跳过认证
        );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if (shouldSkipAuth(path)) {
            log.debug("Skipping auth for path: {}", path);
            return chain.filter(exchange);
        }

        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            log.warn("No token found in request to: {}", path);
            return unauthorized(exchange, "Missing authentication token");
        }

        return validateToken(token)
            .flatMap(jwt -> {
                log.debug("Token validated for user: {}", jwt.getSubject());
                ServerHttpRequest modifiedRequest = addUserHeaders(request, jwt);
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            })
            .onErrorResume(JwtValidationException.class, e -> {
                log.warn("Token validation failed: {}", e.getMessage());
                return unauthorized(exchange, "Invalid or expired token");
            })
            .onErrorResume(Exception.class, e -> {
                log.error("Unexpected error during token validation", e);
                return unauthorized(exchange, "Token validation error");
            });
    }

    private boolean shouldSkipAuth(String path) {
        return skipPaths.stream().anyMatch(path::startsWith);
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        String tokenParam = request.getQueryParams().getFirst("access_token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }

        return null;
    }

    private Mono<Jwt> validateToken(String token) {
        return Mono.fromCallable(() -> jwtDecoder.decode(token));
    }

    private ServerHttpRequest addUserHeaders(ServerHttpRequest request, Jwt jwt) {
        String userId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) {
            username = jwt.getClaimAsString("username");
        }
        if (username == null) {
            username = userId;
        }

        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) {
            roles = jwt.getClaimAsStringList("authorities");
        }
        if (roles == null) {
            roles = jwt.getClaimAsStringList("scope");
        }
        if (roles == null) {
            roles = Collections.emptyList();
        }

        String issuer = jwt.getClaimAsString("iss");
        if (issuer == null) {
            issuer = "";
        }

        List<String> audience = jwt.getAudience();
        String audienceStr = audience != null && !audience.isEmpty() ? String.join(",", audience) : "";

        return request.mutate()
            .header("X-User-Id", userId)
            .header("X-User-Name", username)
            .header("X-User-Roles", String.join(",", roles))
            .header("X-Token-Issuer", issuer)
            .header("X-Token-Audience", audienceStr)
            .build();
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", Instant.now().toString());
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("error", "Unauthorized");
        error.put("message", message);
        error.put("path", exchange.getRequest().getPath().value());

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(error);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Failed to write error response", e);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}