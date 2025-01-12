package com.jfs.pms.service.sprints;

import com.jfs.pms.domain.Sprint;
import com.jfs.pms.domain.User;
import com.jfs.pms.exception.NotFoundException;
import com.jfs.pms.exception.UnAuthorizedException;
import com.jfs.pms.repository.ProjectRepository;
import com.jfs.pms.repository.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class SprintService implements ISprintService {

    private static final Logger log = LoggerFactory.getLogger(SprintService.class);
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;

    public SprintService(SprintRepository sprintRepository, ProjectRepository projectRepository) {
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Sprint addSprint(User loggedInUser, String projectId, Sprint sprint) {
        log.info("Adding sprint to project - {}", projectId);
        try {
            var projectUuid = UUID.fromString(projectId);
            var project = projectRepository.findByPublicId(projectUuid)
                    .orElseThrow(() -> new NotFoundException("Project not found", NOT_FOUND));

            if(!project.getOwner().getId().equals(loggedInUser.getId()) &&
                !project.getCoOwner().getId().equals(loggedInUser.getId())) {
                throw new UnAuthorizedException("User is not authorized to add sprint", UNAUTHORIZED);
            }

            List<Sprint> overlappingSprints = sprintRepository.findOverlappingSprints(
                    project.getId(), sprint.getStartDate(), sprint.getEndDate());

            if(!overlappingSprints.isEmpty()) {
                throw new IllegalArgumentException("Sprint dates overlap with existing sprint - " + overlappingSprints.getFirst().getName());
            }

            sprint.setProject(project);
            return sprintRepository.save(sprint);
        } catch (NotFoundException exception) {
            log.error("Error occurred, Invalid project information passed", exception);
            throw exception;
        }  catch (IllegalArgumentException exception) {
            log.error("Sprint dates overlap with existing sprint", exception);
            throw exception;
        }  catch (UnAuthorizedException exception) {
            log.error("User is not authorized to add sprint", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while adding sprint", exception);
            throw exception;
        }
    }

    @Override
    public Sprint updateSprint(User loggedInUser, Sprint sprint) {
        log.info("Updating sprint - {}", sprint.getId());
        try {
            var existingSprint = sprintRepository.findById(sprint.getId())
                    .orElseThrow(() -> new NotFoundException("Sprint not found", NOT_FOUND));

            if(!existingSprint.getProject().getOwner().getId().equals(loggedInUser.getId()) &&
                    !existingSprint.getProject().getCoOwner().getId().equals(loggedInUser.getId())) {
                throw new UnAuthorizedException("User is not authorized to update sprint information", UNAUTHORIZED);
            }
            List<Sprint> overlappingSprints = sprintRepository.findOverlappingSprints(
                    existingSprint.getProject().getId(), sprint.getStartDate(), sprint.getEndDate());

            if(!overlappingSprints.isEmpty()) {
                throw new IllegalArgumentException("Sprint dates overlap with existing sprint - " + overlappingSprints.getFirst().getName());
            }

            existingSprint.setName(sprint.getName());
            existingSprint.setSprintGoal(sprint.getSprintGoal());
            existingSprint.setStartDate(sprint.getStartDate());
            existingSprint.setEndDate(sprint.getEndDate());
            return sprintRepository.save(existingSprint);
        } catch (UnAuthorizedException exception) {
            log.error("User is not authorized to update sprint", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while updating sprint", exception);
            throw exception;
        }
    }

    @Override
    public boolean deleteSprint(User loggedInUser, long sprintId) {
        try {
            var sprint = sprintRepository.findById(sprintId)
                    .orElseThrow(() -> new NotFoundException("Sprint not found", NOT_FOUND));

            if(!sprint.getProject().getOwner().getId().equals(loggedInUser.getId()) &&
                    !sprint.getProject().getCoOwner().getId().equals(loggedInUser.getId())) {
                throw new UnAuthorizedException("User is not authorized to update sprint information", UNAUTHORIZED);
            }

            sprintRepository.delete(sprint);
        } catch (NotFoundException exception) {
            log.error("Error occurred, Invalid sprint information passed", exception);
            throw exception;
        } catch (UnAuthorizedException exception) {
            log.error("User is not authorized to delete sprint", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while deleting sprint", exception);
            throw exception;
        }
        return true;
    }

    @Override
    public Sprint getCurrentSprintForProject(User loggedInUser, String projectId) {
        try {
            var projectUuid = UUID.fromString(projectId);
            var project = projectRepository.findByPublicId(projectUuid)
                    .orElseThrow(() -> new NotFoundException("Project not found", NOT_FOUND));

            if(!project.getOwner().getId().equals(loggedInUser.getId()) &&
                    !project.getCoOwner().getId().equals(loggedInUser.getId()) &&
                    project.getTeamMembers().stream().noneMatch(user -> user.getId().equals(loggedInUser.getId()))) {
                throw new UnAuthorizedException("User is not authorized to get current sprint for project ID " + projectId, UNAUTHORIZED);
            }

            var currentSprint = sprintRepository.findCurrentSprint(project.getId());
            if(currentSprint == null) {
                throw new NotFoundException("No active sprint found for project ID " + projectId, NOT_FOUND);
            }
            return currentSprint;
        } catch (NotFoundException exception) {
            log.error("Error occurred, Invalid project information passed", exception);
            throw exception;
        } catch (UnAuthorizedException exception) {
            log.error("User is not authorized to get current sprint", exception);
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while getting current sprint", exception);
            throw exception;
        }
    }
}
