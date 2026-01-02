package org.r2learning.project.interfaces.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.r2learning.common.utils.UserContextHolder;
import org.r2learning.project.application.cmd.CreateProjectCmd;
import org.r2learning.project.application.service.ProjectApplicationService;
import org.r2learning.project.interfaces.web.dto.ProjectDTO;
import org.r2learning.project.interfaces.web.dto.ProjectStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectApplicationService projectApplicationService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> get() {
        return ResponseEntity.ok(projectApplicationService.get());
    }

    @PostMapping
    public ResponseEntity<Long> createProject(@RequestBody CreateProjectCmd cmd) {
        Long ownerId = UserContextHolder.getCurrentUserId();
        cmd.setOwnerId(ownerId);
        Long projectId = projectApplicationService.createProject(cmd);
        return ResponseEntity.ok(projectId);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ProjectDTO> getProjectDetails(@PathVariable Long id) {
        return ResponseEntity.ok(projectApplicationService.getProjectWithTasks(id));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<ProjectStatsDTO> getProjectStats(
        @PathVariable Long id) {
        return ResponseEntity.ok(projectApplicationService.getProjectStats(id));
    }
}
