package org.r2learning.task.interfaces.web.dto;

import lombok.Data;
import org.r2learning.task.domain.task.valobj.TaskPriority;
import org.r2learning.task.domain.task.valobj.TaskStatus;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
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
}
