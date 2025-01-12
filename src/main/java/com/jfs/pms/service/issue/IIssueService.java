package com.jfs.pms.service.issue;

import com.jfs.pms.domain.Issue;
import com.jfs.pms.domain.User;

import java.util.List;

public interface IIssueService {

    Issue addIssue(User loggedInUser, Long sprintId, Issue issue);
    Issue updateIssue(User loggedInUser, Issue issue);
    boolean deleteIssue(Long issueId);
    Issue getIssue(Long issueId);
    List<Issue> getAllIssueForSprint(Long sprintId);
}
