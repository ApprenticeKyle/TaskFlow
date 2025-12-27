package org.r2learning.task.controller;

import org.r2learning.task.model.Task;
import org.r2learning.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getTasks(@RequestParam Long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }

    @PostMapping
    public Map<String, Object> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("data", createdTask);
        return response;
    }
}
