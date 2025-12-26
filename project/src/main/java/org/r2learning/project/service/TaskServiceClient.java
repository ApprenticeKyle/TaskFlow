package org.r2learning.project.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TaskServiceClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<Map> getTasksByProjectId(Long projectId) {
        return webClientBuilder
            .build()
            .get()
            .uri("lb://task-service/tasks?projectId=" + projectId)
            .retrieve()
            .bodyToMono(Map.class);
    }

    public Mono<Map> createTaskForProject(Long projectId, Map<String, Object> taskData) {
        return webClientBuilder
            .build()
            .post()
            .uri("lb://task-service/tasks")
            .bodyValue(taskData)
            .retrieve()
            .bodyToMono(Map.class);
    }
}
