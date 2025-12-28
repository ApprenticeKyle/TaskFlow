package org.r2learning.project.interfaces.web;

import lombok.RequiredArgsConstructor;
import org.r2learning.project.application.cmd.CreateProjectCmd;
import org.r2learning.project.application.service.ProjectApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.r2learning.project.interfaces.web.dto.ProjectDTO;
import org.r2learning.project.interfaces.web.dto.ProjectStatsDTO;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectApplicationService projectApplicationService;

    @PostMapping
    public ResponseEntity<Long> createProject(@RequestBody CreateProjectCmd cmd) {
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
