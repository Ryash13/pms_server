package com.jfs.pms.repository;

import com.jfs.pms.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByPublicId(UUID publicId);

    @Query(value = "SELECT p.* FROM jfs_pms_projects p " +
            "LEFT JOIN jfs_pms_project_teams pt ON p.id = pt.project_id " +
            "WHERE p.owner_id = :userId " +
            "OR p.co_owner_id = :userId " +
            "OR pt.user_id = :userId", nativeQuery = true)
    List<Project> getProjectsByLoggedInUser(@Param("userId") Long userId);
    boolean existsByName(String name);
}
