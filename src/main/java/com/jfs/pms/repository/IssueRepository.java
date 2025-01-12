package com.jfs.pms.repository;

import com.jfs.pms.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
