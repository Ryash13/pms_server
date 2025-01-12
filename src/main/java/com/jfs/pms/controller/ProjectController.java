package com.jfs.pms.controller;

import com.jfs.pms.domain.User;
import com.jfs.pms.dto.project.ProjectRequestDto;
import com.jfs.pms.exception.UnAuthorizedException;
import com.jfs.pms.service.project.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.jfs.pms.constants.Constants.INVALID_ATTEMPT;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody ProjectRequestDto requestDto) {
        log.info("Executing addProject method");
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(projectService.addProject(loggedInUser, requestDto), CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getProjects() {
        log.info("Executing getProjects method");
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(projectService.getProjects(loggedInUser), OK);
    }

    @PatchMapping("/{projectId}/status/{status}")
    public ResponseEntity<?> updateProjectStatus(@PathVariable String projectId, @PathVariable String status) {
        log.info("Executing updateProjectStatus method");
        var loggedInUser = getLoggedInUser();
        projectService.updateProjectStatus(loggedInUser, projectId, status);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PutMapping("/addMember/{projectId}/{memberId}")
    public ResponseEntity<?> addTeamMember(@PathVariable String projectId, @PathVariable String memberId) {
        log.info("Executing addTeamMember method");
        var loggedInUser = getLoggedInUser();
        projectService.addMemberToProjectTeam(loggedInUser,projectId, memberId);
        return new ResponseEntity<>(NO_CONTENT);
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
