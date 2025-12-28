package org.r2learning.project.domain.project.gateway;

import org.r2learning.project.domain.project.Project;

public interface ProjectGateway {
    Project save(Project project);

    Project findById(Long id);

    void delete(Long id);
}
