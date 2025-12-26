package org.r2learning.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class AuthController {

    @PostMapping("/login")
    public Mono<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("data", Map.of(
            "token", "mock-jwt-token-123456",
            "userId", "1",
            "username", credentials.get("username")
        ));
        return Mono.just(result);
    }

    @PostMapping("/register")
    public Mono<Map<String, Object>> register(@RequestBody Map<String, String> userInfo) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "注册成功");
        result.put("data", userInfo);
        return Mono.just(result);
    }
}
