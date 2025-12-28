package org.r2learning.project.interfaces.web.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskRemoteDTO {
    private Long id;
    private String title;
    private String description;
    private String status; // String for simplicity in remote
    private String priority;
    private Long assigneeId;
    private Long reporterId;
    private Long projectId;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
