package org.r2learning.project.interfaces.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStatsDTO {
    private int activeTasks;
    private int completedTasks;
    private int highPriorityTasks;
    private String teamVelocity;
}
