package com.jfs.pms.controller;

import com.jfs.pms.domain.User;
import com.jfs.pms.dto.issue.IssueRequest;
import com.jfs.pms.exception.UnAuthorizedException;
import com.jfs.pms.service.issue.IIssueService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.jfs.pms.constants.Constants.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/issue")
public class IssueController {

    public static final Logger log = LoggerFactory.getLogger(IssueController.class);
    private final IIssueService issueService;

    public IssueController(IIssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping("/add/{sprintId}")
    public ResponseEntity<?> addIssue(@PathVariable Long sprintId, @RequestBody @Valid IssueRequest issue) {
        log.info("Adding issue for sprintId: {}", sprintId);
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(issueService.addIssue(loggedInUser, sprintId, issue), CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateIssue(@RequestBody @Valid IssueRequest issue) {
        log.info("Updating issue for issueId: {}", issue.getId());
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(issueService.updateIssue(loggedInUser, issue), OK);
    }

    @DeleteMapping("/delete/{issueId}")
    public ResponseEntity<?> deleteIssue(@PathVariable Long issueId) {
        log.info("Deleting issue for issueId: {}", issueId);
        var message = issueService.deleteIssue(issueId) ? ISSUE_DELETE_SUCCESS : ISSUE_DELETE_FAILED;
        return new ResponseEntity<>(message, OK);
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<?> getIssue(@PathVariable Long issueId) {
        log.info("Getting issue for issueId: {}", issueId);
        return new ResponseEntity<>(issueService.getIssue(issueId), OK);
    }

    @GetMapping("/all/{sprintId}")
    public ResponseEntity<?> getAllIssueForSprint(@PathVariable Long sprintId) {
        log.info("Getting all issues for sprintId: {}", sprintId);
        return new ResponseEntity<>(issueService.getAllIssueForSprint(sprintId), OK);
    }

    // Helper function to get the logged-in user
    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Invalid user attempted to add new project, auth used: {}", authentication);
            throw new UnAuthorizedException(INVALID_ATTEMPT, UNAUTHORIZED);
        }
        return (User) authentication.getPrincipal();
    }
}
