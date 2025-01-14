package com.jfs.pms.service.project;

import com.jfs.pms.constants.enums.ProjectStatus;
import com.jfs.pms.domain.User;
import com.jfs.pms.dto.project.ProjectRequestDto;
import com.jfs.pms.dto.project.ProjectResponse;
import com.jfs.pms.exception.AlreadyExistsException;
import com.jfs.pms.exception.NotFoundException;
import com.jfs.pms.exception.UnAuthorizedException;
import com.jfs.pms.repository.ClientRepository;
import com.jfs.pms.repository.ProjectRepository;
import com.jfs.pms.repository.UserRepository;
import com.jfs.pms.utility.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class ProjectService implements IProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, ClientRepository clientRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProjectResponse addProject(User loggedInUser, ProjectRequestDto projectDto) {
        try {
            var project = Mapper.fromProjectRequestToProject(projectDto);
            if (projectAlreadyExists(project.getName())) {
                log.error("Project with name {} already exists", project.getName());
                throw new AlreadyExistsException("Project with name " + project.getName() + " already exists", BAD_REQUEST);
            }
            var client = clientRepository.findByPublicId(UUID.fromString(projectDto.getClientId()))
                    .orElseThrow(() -> new NotFoundException("Client not found with public id : " + projectDto.getClientId(), BAD_REQUEST));
            project.setOwner(loggedInUser);
            project.setClient(client);
            projectRepository.save(project);
            return Mapper.fromProjectToProjectResponse(project);
        } catch (AlreadyExistsException exception) {
            log.error("Error occurred while adding project, project name already exists: {}", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while adding project: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void updateProject(ProjectRequestDto project) {

    }

    @Override
    public boolean deleteProject(User loggedInUser, String publicId) {
        try {
            UUID uuid = UUID.fromString(publicId);
            var project = projectRepository.findByPublicId(uuid)
                    .orElseThrow(() -> new NotFoundException("Project not found with public id : " + publicId, BAD_REQUEST));
            if (!project.getOwner().getId().equals(loggedInUser.getId())) {
                log.error("User with id {} is not authorized to delete project with id {}", loggedInUser.getId(), publicId);
                throw new UnAuthorizedException("User is not authorized to delete the project", UNAUTHORIZED);
            }
            projectRepository.delete(project);
            return true;
        } catch (UnAuthorizedException exception) {
            log.error("Error occurred while deleting project, user is not authorized: {}", exception.getMessage());
            throw exception;
        }catch (Exception exception) {
            log.error("Error occurred while deleting project: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public List<ProjectResponse> getProjects(User loggedInUser) {
        try {
            var projects = projectRepository.getProjectsByLoggedInUser(loggedInUser.getId());
            return Mapper.fromProjectsToProjectResponse(projects);
        } catch (Exception exception) {
            log.error("Error occurred while fetching projects: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public void updateProjectStatus(User loggedInUser, String publicId, String status) {
        try {
            var uuid = UUID.fromString(publicId);
            var project = projectRepository.findByPublicId(uuid)
                    .orElseThrow(() -> new NotFoundException("Project not found with public id : " + publicId, BAD_REQUEST));
            if (!project.getOwner().getId().equals(loggedInUser.getId())) {
                log.error("User with id {} is not authorized to update project status with id {}", loggedInUser.getId(), publicId);
                throw new UnAuthorizedException("User is not authorized to update the project status", UNAUTHORIZED);
            }
            var projectStatus = ProjectStatus.valueOf(status);
            project.setStatus(projectStatus);
            projectRepository.save(project);
        }catch (NotFoundException exception) {
            log.error("Error occurred while updating project status, Project Not Found: {}", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while updating project status: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public boolean addMemberToProjectTeam(User loggeedInUser, String projectId, String memberId) {
        try {
            if(loggeedInUser.getPublicId().toString().equalsIgnoreCase(memberId)) {
                log.error("User with id {} is owner, cannot add himself to project team", loggeedInUser.getId());
                throw new UnAuthorizedException("User is the owner, cannot add himself to project team", UNAUTHORIZED);
            }
            var projectUuid = UUID.fromString(projectId);
            var memberUuid = UUID.fromString(memberId);
            var project = projectRepository.findByPublicId(projectUuid)
                    .orElseThrow(() -> new NotFoundException("Project not found with public id : " + projectId, BAD_REQUEST));

            if(!loggeedInUser.getPublicId().toString().equalsIgnoreCase(project.getOwner().getPublicId().toString())
            && !loggeedInUser.getPublicId().toString().equalsIgnoreCase(project.getCoOwner().getPublicId().toString())
            ) {
                log.error("User with id {} is not authorized to add member to project team", loggeedInUser.getId());
                throw new UnAuthorizedException("User is not authorized to add member to project team", UNAUTHORIZED);
            }

            var member = userRepository.findByPublicId(memberUuid)
                    .orElseThrow(() -> new NotFoundException("User not found with public id : " + memberId, BAD_REQUEST));
            project.getTeamMembers().add(member);
            projectRepository.save(project);
            return true;
        } catch (NotFoundException exception) {
            log.error("Error occurred while adding member to project team, Project Not Found or User Not Found: {}", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while adding member to project team: {}", exception.getMessage());
            throw exception;
        }
    }

    private boolean projectAlreadyExists(String name) {
        return projectRepository.existsByName(name);
    }
}
