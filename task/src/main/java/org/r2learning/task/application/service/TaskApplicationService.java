package org.r2learning.task.application.service;

import lombok.RequiredArgsConstructor;
import org.r2learning.task.application.cmd.CreateTaskCmd;
import org.r2learning.task.domain.task.Task;
import org.r2learning.task.domain.task.gateway.TaskGateway;
import org.r2learning.task.infrastructure.db.mapper.TaskMapper;
import org.r2learning.task.interfaces.web.dto.TaskDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Task Application Service
 * 应用服务：编排业务逻辑，不包含领域逻辑
 */
@Service
@RequiredArgsConstructor
public class TaskApplicationService {

    private final TaskGateway taskGateway;
    private final TaskMapper taskMapper;

    @Transactional
    public Long createTask(CreateTaskCmd cmd) {
        // 1. Convert Command to Domain Entity (using Factory)
        Task task = Task.create(
                cmd.getTitle(),
                cmd.getDescription(),
                cmd.getPriority(),
                cmd.getAssigneeId(),
                cmd.getReporterId(),
                cmd.getProjectId(),
                cmd.getDueDate());

        // 2. Persist using Gateway
        Task savedTask = taskGateway.save(task);

        // 3. Return ID (or DTO)
        return savedTask.getId();
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByProjectId(Long projectId) {
        return taskGateway.findByProjectId(projectId).stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }
}
