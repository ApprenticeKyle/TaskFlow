package org.r2learning.task.infrastructure.db.dataobject;

import jakarta.persistence.*;
import lombok.Data;
import org.r2learning.task.domain.task.valobj.TaskPriority;
import org.r2learning.task.domain.task.valobj.TaskStatus;

import java.time.LocalDateTime;

/**
 * Task Data Object (JPA Entity)
 * 数据库映射对象，纯数据结构
 */
@Data
@Entity
@Table(name = "tasks")
public class TaskDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private Long assigneeId;
    private Long reporterId;
    private Long projectId;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
