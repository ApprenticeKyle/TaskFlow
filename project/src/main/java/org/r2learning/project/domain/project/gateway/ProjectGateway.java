package org.r2learning.project.domain.project.gateway;

import java.util.Collection;

import org.r2learning.project.domain.project.Project;

public interface ProjectGateway {
    Project save(Project project);

    Project findById(Long id);

    void delete(Long id);

    Collection<Project> findAll();
}
