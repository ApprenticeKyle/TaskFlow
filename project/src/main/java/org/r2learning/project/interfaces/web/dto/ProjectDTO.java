package org.r2learning.project.interfaces.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private List<TaskRemoteDTO> tasks;
    private String status;
    private LocalDate deadline;
    private Integer members;
    private Integer progress;
}
