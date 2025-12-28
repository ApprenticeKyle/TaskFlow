package org.r2learning.task.infrastructure.db.repository;

import lombok.RequiredArgsConstructor;
import org.r2learning.task.domain.task.Task;
import org.r2learning.task.domain.task.gateway.TaskGateway;
import org.r2learning.task.infrastructure.db.dataobject.TaskDO;
import org.r2learning.task.infrastructure.db.mapper.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Task Repository Implementation
 * 在Infrastructure层实现Domain层的Gateway接口
 * 负责Entity和DO之间的转换
 */
@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskGateway {

    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task save(Task task) {
        TaskDO taskDO = taskMapper.toDO(task);
        TaskDO savedDO = jpaTaskRepository.save(taskDO);
        return taskMapper.toEntity(savedDO);
    }

    @Override
    public Task findById(Long id) {
        return jpaTaskRepository.findById(id).map(taskMapper::toEntity).orElse(null);
    }

    @Override
    public List<Task> findByProjectId(Long projectId) {
        return jpaTaskRepository.findByProjectId(projectId).stream()
                .map(taskMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        jpaTaskRepository.deleteById(id);
    }
}
