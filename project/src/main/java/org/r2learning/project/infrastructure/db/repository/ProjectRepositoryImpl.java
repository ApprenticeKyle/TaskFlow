package org.r2learning.project.infrastructure.db.repository;

import lombok.RequiredArgsConstructor;
import org.r2learning.project.domain.project.Project;
import org.r2learning.project.domain.project.gateway.ProjectGateway;
import org.r2learning.project.infrastructure.db.dataobject.ProjectDO;
import org.r2learning.project.infrastructure.db.mapper.ProjectMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectGateway {
    private final JpaProjectRepository jpaProjectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public Project save(Project project) {
        ProjectDO projectDO = projectMapper.toDO(project);
        ProjectDO savedDO = jpaProjectRepository.save(projectDO);
        return projectMapper.toEntity(savedDO);
    }

    @Override
    public Project findById(Long id) {
        return jpaProjectRepository.findById(id).map(projectMapper::toEntity).orElse(null);
    }

    @Override
    public void delete(Long id) {
        jpaProjectRepository.deleteById(id);
    }
}
