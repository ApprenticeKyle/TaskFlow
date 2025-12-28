package org.r2learning.task.interfaces.web;

import lombok.RequiredArgsConstructor;
import org.r2learning.task.application.cmd.CreateTaskCmd;
import org.r2learning.task.application.service.TaskApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import org.r2learning.task.interfaces.web.dto.TaskDTO;

/**
 * Task Controller (Interface Layer)
 * 适配HTTP请求，调用Application Service
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskApplicationService taskApplicationService;

    @PostMapping
    public ResponseEntity<Long> createTask(@RequestBody CreateTaskCmd cmd) {
        Long taskId = taskApplicationService.createTask(cmd);
        return ResponseEntity.ok(taskId);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasksByProjectId(@RequestParam Long projectId) {
        return ResponseEntity.ok(taskApplicationService.getTasksByProjectId(projectId));
    }
}
