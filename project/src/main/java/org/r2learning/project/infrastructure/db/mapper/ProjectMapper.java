package org.r2learning.project.infrastructure.db.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.r2learning.project.domain.project.Project;
import org.r2learning.project.infrastructure.db.dataobject.ProjectDO;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDO toDO(Project project);

    Project toEntity(ProjectDO projectDO);
}
