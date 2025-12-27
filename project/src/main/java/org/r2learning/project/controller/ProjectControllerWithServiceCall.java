package org.r2learning.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.r2learning.project.service.TaskServiceClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class ProjectControllerWithServiceCall {

    @Autowired
    private TaskServiceClient taskServiceClient;

    @GetMapping("/{id}/tasks")
    public Mono<Map<String, Object>> getProjectTasks(@PathVariable Long id) {
        return taskServiceClient.getTasksByProjectId(id)
                .map(tasks -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("code", 200);
                    result.put("message", "获取项目任务成功");
                    result.put("data", tasks);
                    return result;
                });
    }
}
