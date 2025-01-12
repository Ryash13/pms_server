package com.jfs.pms.repository;

import com.jfs.pms.domain.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    @Query("SELECT s FROM Sprint s WHERE s.project.id = :projectId AND " +
            "((:startDate BETWEEN s.startDate AND s.endDate) OR " +
            "(:endDate BETWEEN s.startDate AND s.endDate) OR " +
            "(s.startDate BETWEEN :startDate AND :endDate) OR " +
            "(s.endDate BETWEEN :startDate AND :endDate))")
    List<Sprint> findOverlappingSprints(@Param("projectId") Long projectId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    List<Sprint> findByProjectId(Long projectId);

    @Query("SELECT s FROM Sprint s WHERE s.project.id = :projectId AND " +
            "(CURRENT_DATE = s.startDate OR CURRENT_DATE = s.endDate OR " +
            "CURRENT_DATE BETWEEN s.startDate AND s.endDate)")
    Sprint findCurrentSprint(@Param("projectId") Long projectId);
}
