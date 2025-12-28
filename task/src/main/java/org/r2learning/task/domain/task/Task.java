package org.r2learning.task.domain.task;

import org.r2learning.task.domain.task.valobj.TaskPriority;
import org.r2learning.task.domain.task.valobj.TaskStatus;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Task Aggregate Root
 * 任务聚合根
 */
@Getter
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;
    private Long reporterId;
    private Long projectId;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Construtor for reconstruction
    public Task(Long id, String title, String description, TaskStatus status, TaskPriority priority, Long assigneeId,
            Long reporterId, Long projectId, LocalDateTime dueDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assigneeId = assigneeId;
        this.reporterId = reporterId;
        this.projectId = projectId;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory method for creating new task
    // 采用工厂方法创建，封装初始化逻辑
    public static Task create(String title, String description, TaskPriority priority, Long assigneeId, Long reporterId,
            Long projectId,
            LocalDateTime dueDate) {
        Task task = new Task(null, title, description, TaskStatus.TODO, priority, assigneeId, reporterId, projectId,
                dueDate,
                LocalDateTime.now(), LocalDateTime.now());
        // Domain rules check
        task.validate();
        return task;
    }

    private void validate() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (reporterId == null) {
            throw new IllegalArgumentException("Reporter is required");
        }
    }

    // Domain Behaviors
    // 领域行为：开始任务
    public void start() {
        if (this.status != TaskStatus.TODO) {
            throw new IllegalStateException("Only TODO tasks can be started");
        }
        this.status = TaskStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    // 领域行为：完成任务
    public void complete() {
        this.status = TaskStatus.DONE;
        this.updatedAt = LocalDateTime.now();
    }

    // 领域行为：更改标题
    public void changeTitle(String newTitle) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = newTitle;
        this.updatedAt = LocalDateTime.now();
    }
}
