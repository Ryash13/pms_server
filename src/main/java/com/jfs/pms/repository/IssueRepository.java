package com.jfs.pms.repository;

import com.jfs.pms.domain.Issue;
import com.jfs.pms.domain.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {


    List<Issue> findAllBySprintId(Long sprintId);
}
