package org.r2learning.task.domain.task.gateway;

import org.r2learning.task.domain.task.Task;

/**
 * Task Gateway (Repository Interface in Domain Layer)
 * 依赖倒置：Domain层定义接口，Infrastructure层实现
 */
public interface TaskGateway {
    Task save(Task task);

    Task findById(Long id);

    java.util.List<Task> findByProjectId(Long projectId);

    void delete(Long id);
}
