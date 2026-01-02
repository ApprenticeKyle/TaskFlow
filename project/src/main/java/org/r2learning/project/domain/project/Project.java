package org.r2learning.project.domain.project;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Project Aggregate Root
 */
@Getter
public class Project {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private LocalDate deadline;
    private Integer members;
    private Integer progress;

    public Project(Long id, String name, String description, Long ownerId, String status, LocalDate deadline,
            Integer members, Integer progress, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.status = status;
        this.deadline = deadline;
        this.members = members;
        this.progress = progress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Project create(String name, String description, Long ownerId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        return new Project(null, name, description, ownerId, "planning", null, 0, 0, LocalDateTime.now(),
                LocalDateTime.now());
    }
}
