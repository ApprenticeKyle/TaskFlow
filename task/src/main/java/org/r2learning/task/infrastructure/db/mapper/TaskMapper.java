package org.r2learning.task.infrastructure.db.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.r2learning.task.domain.task.Task;
import org.r2learning.task.infrastructure.db.dataobject.TaskDO;
import org.r2learning.task.interfaces.web.dto.TaskDTO;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDO toDO(Task task);

    Task toEntity(TaskDO taskDO);

    TaskDTO toDTO(Task task);
}
