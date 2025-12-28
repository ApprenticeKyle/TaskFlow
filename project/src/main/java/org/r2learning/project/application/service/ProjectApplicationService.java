package org.r2learning.project.application.service;

import lombok.RequiredArgsConstructor;
import org.r2learning.project.application.cmd.CreateProjectCmd;
import org.r2learning.project.client.TaskFeignClient;
import org.r2learning.project.domain.project.Project;
import org.r2learning.project.domain.project.gateway.ProjectGateway;
import org.r2learning.project.interfaces.web.dto.ProjectDTO;
import org.r2learning.project.interfaces.web.dto.TaskRemoteDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectApplicationService {

    private final ProjectGateway projectGateway;
    private final TaskFeignClient taskFeignClient;

    @Transactional
    public Long createProject(CreateProjectCmd cmd) {
        Project project = Project.create(cmd.getName(), cmd.getDescription(), cmd.getOwnerId());
        return projectGateway.save(project).getId();
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProjectWithTasks(Long projectId) {
        Project project = projectGateway.findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        // RPC call to Task Module using structured DTO
        List<TaskRemoteDTO> tasks = taskFeignClient.getTasksByProjectId(projectId);

        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwnerId())
                .tasks(tasks)
                .build();
    }
}
