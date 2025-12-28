package org.r2learning.project.infrastructure.db.repository;

import org.r2learning.project.infrastructure.db.dataobject.ProjectDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProjectRepository extends JpaRepository<ProjectDO, Long> {
}
