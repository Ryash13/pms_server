package com.jfs.pms.service.issue;

import com.jfs.pms.domain.User;
import com.jfs.pms.dto.issue.IssueRequest;
import com.jfs.pms.dto.issue.IssueResponseDto;
import com.jfs.pms.exception.NotFoundException;
import com.jfs.pms.exception.UnAuthorizedException;
import com.jfs.pms.repository.IssueRepository;
import com.jfs.pms.repository.SprintRepository;
import com.jfs.pms.utility.Mapper;
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
    public IssueResponseDto addIssue(User loggedInUser, Long sprintId, IssueRequest issueRequest) {
        try {
            var issue = Mapper.fromIssueRequestToIssue(issueRequest);
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
    public IssueResponseDto updateIssue(User loggedInUser, IssueRequest issueRequest) {
        try {
            var issue = Mapper.fromIssueRequestToIssue(issueRequest);
            if(!loggedInUser.getId().equals(issue.getAssigner().getId())) {
                throw new UnAuthorizedException("User not authorized to update issue", HttpStatus.UNAUTHORIZED);
            }
            var savedIssue = issueRepository.save(issue);
            return Mapper.fromIssueToIssueResponse(savedIssue);
        } catch (UnAuthorizedException exception) {
            log.error("User not authorized to update issue", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("Error while updating issue", exception);
            throw exception;
        }
    }

    @Override
    public boolean deleteIssue(Long issueId) {
        try {
            var issue = issueRepository.findById(issueId)
                    .orElseThrow(() -> new NotFoundException("Issue not found", NOT_FOUND));
            issueRepository.delete(issue);
        } catch (NotFoundException exception) {
            log.error("Issue not found with ID {}", issueId);
            throw exception;
        } catch (Exception exception) {
            log.error("Error while deleting issue", exception);
            throw exception;
        }
        return true;
    }

    @Override
    public IssueResponseDto getIssue(Long issueId) {
        try {
            var issue =  issueRepository.findById(issueId)
                    .orElseThrow(() -> new NotFoundException("Issue not found", NOT_FOUND));
            return Mapper.fromIssueToIssueResponse(issue);
        } catch(NotFoundException exception) {
            log.error("Issue not found with ID :: {}", issueId);
            throw exception;
        } catch (Exception exception) {
            log.error("Error while fetching issue", exception);
            throw exception;
        }
    }

    @Override
    public List<IssueResponseDto> getAllIssueForSprint(Long sprintId) {
        try {
            var issues = issueRepository.findAllBySprintId(sprintId);
            return Mapper.fromIssuesToIssueResponse(issues);
        } catch (Exception exception) {
            log.error("Error while fetching issues for sprint", exception);
            throw exception;
        }
    }
}
