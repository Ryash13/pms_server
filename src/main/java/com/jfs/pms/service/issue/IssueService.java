package com.jfs.pms.service.issue;

import com.jfs.pms.domain.Issue;
import com.jfs.pms.domain.User;
import com.jfs.pms.exception.NotFoundException;
import com.jfs.pms.repository.IssueRepository;
import com.jfs.pms.repository.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class IssueService implements IIssueService {

    private static final Logger log = LoggerFactory.getLogger(IssueService.class);
    private final IssueRepository issueRepository;
    private final SprintRepository sprintRepository;

    public IssueService(IssueRepository issueRepository, SprintRepository sprintRepository) {
        this.issueRepository = issueRepository;
        this.sprintRepository = sprintRepository;
    }

    @Override
    public Issue addIssue(User loggedInUser, Long sprintId, Issue issue) {
        try {
            var sprint = sprintRepository.findById(sprintId)
                    .orElseThrow(() -> new NotFoundException("Sprint not found", NOT_FOUND));
            issue.setSprint(sprint);
            issue.setAssigner(loggedInUser);
        } catch (Exception exception) {
            log.error("Error while adding issue", exception);
            throw exception;
        }
        return null;
    }

    @Override
    public Issue updateIssue(User loggedInUser, Issue issue) {
        return null;
    }

    @Override
    public boolean deleteIssue(Long issueId) {
        return false;
    }

    @Override
    public Issue getIssue(Long issueId) {
        return null;
    }

    @Override
    public List<Issue> getAllIssueForSprint(Long sprintId) {
        return List.of();
    }
}
