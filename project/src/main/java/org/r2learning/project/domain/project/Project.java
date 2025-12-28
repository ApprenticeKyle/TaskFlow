package org.r2learning.project.domain.project;

import lombok.Getter;

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

    public Project(Long id, String name, String description, Long ownerId, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Project create(String name, String description, Long ownerId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        return new Project(null, name, description, ownerId, LocalDateTime.now(), LocalDateTime.now());
    }
}
