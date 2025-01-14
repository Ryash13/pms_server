package com.jfs.pms.service.issue;

import com.jfs.pms.domain.Issue;
import com.jfs.pms.domain.User;
import com.jfs.pms.dto.issue.IssueRequest;
import com.jfs.pms.dto.issue.IssueResponseDto;

import java.util.List;

public interface IIssueService {

    IssueResponseDto addIssue(User loggedInUser, Long sprintId, IssueRequest issue);
    IssueResponseDto updateIssue(User loggedInUser, IssueRequest issue);
    boolean deleteIssue(Long issueId);
    IssueResponseDto getIssue(Long issueId);
    List<IssueResponseDto> getAllIssueForSprint(Long sprintId);
}
