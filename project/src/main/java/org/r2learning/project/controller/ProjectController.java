package org.r2learning.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @GetMapping
    public Mono<Map<String, Object>> listProjects() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取项目列表成功");
        result.put("data", new String[]{"项目1", "项目2", "项目3"});
        return Mono.just(result);
    }

    @GetMapping("/{id}")
    public Mono<Map<String, Object>> getProject(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取项目详情成功");
        result.put("data", Map.of(
            "id", id,
            "name", "项目" + id,
            "description", "这是项目" + id + "的描述"
        ));
        return Mono.just(result);
    }

    @PostMapping
    public Mono<Map<String, Object>> createProject(@RequestBody Map<String, Object> project) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "创建项目成功");
        result.put("data", project);
        return Mono.just(result);
    }
}
