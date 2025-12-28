package org.r2learning.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.r2learning.project.interfaces.web.dto.TaskRemoteDTO;

import java.util.List;

@FeignClient(name = "task-service")
public interface TaskFeignClient {

    @GetMapping("/api/tasks")
    List<TaskRemoteDTO> getTasksByProjectId(@RequestParam("projectId") Long projectId);

    @PostMapping("/api/tasks")
    Long createTask(@RequestBody Object taskData);
}
