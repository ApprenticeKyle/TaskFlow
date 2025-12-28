package org.r2learning.task.application.cmd;

import lombok.Data;
import org.r2learning.task.domain.task.valobj.TaskPriority;

import java.time.LocalDateTime;

/**
 * Command Object for Creating Task
 * 应用层入参
 */
@Data
public class CreateTaskCmd {
    private String title;
    private String description;
    private TaskPriority priority;
    private Long assigneeId;
    private Long reporterId;
    private Long projectId;
    private LocalDateTime dueDate;
}
