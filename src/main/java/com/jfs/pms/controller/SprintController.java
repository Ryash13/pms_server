package com.jfs.pms.controller;

import com.jfs.pms.domain.Sprint;
import com.jfs.pms.domain.User;
import com.jfs.pms.exception.UnAuthorizedException;
import com.jfs.pms.service.sprints.ISprintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.jfs.pms.constants.Constants.INVALID_ATTEMPT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/api/v1/sprints")
public class SprintController {

    private static final Logger log = LoggerFactory.getLogger(SprintController.class);
    private final ISprintService sprintService;

    public SprintController(ISprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping("/currentSprint/{projectId}")
    public ResponseEntity<?> getCurrentSpringForProject(@PathVariable String projectId) {
        log.info("Fetching current sprint for project with id: {}", projectId);
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(sprintService.getCurrentSprintForProject(loggedInUser, projectId), OK);
    }

    @PostMapping("/addSprint/{projectId}")
    public ResponseEntity<?> addSprintToProject(@RequestBody Sprint sprint, @PathVariable String projectId) {
        log.info("Adding new sprint to project with id: {}", projectId);
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(sprintService.addSprint(loggedInUser, projectId, sprint), CREATED);
    }

    @PutMapping("/updateSprint")
    public ResponseEntity<?> updateSprint(@RequestBody Sprint sprint) {
        log.info("Updating sprint info with id: {}", sprint.getId());
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(sprintService.updateSprint(loggedInUser, sprint), OK);
    }
    

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Invalid user attempted to add new project, auth used: {}", authentication);
            throw new UnAuthorizedException(INVALID_ATTEMPT, UNAUTHORIZED);
        }
        return (User) authentication.getPrincipal();
    }
}
