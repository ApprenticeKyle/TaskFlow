package org.r2learning.task.infrastructure.db.repository;

import org.r2learning.task.infrastructure.db.dataobject.TaskDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTaskRepository extends JpaRepository<TaskDO, Long> {
    java.util.List<TaskDO> findByProjectId(Long projectId);
}
