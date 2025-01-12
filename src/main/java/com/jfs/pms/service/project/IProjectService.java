package com.jfs.pms.service.project;

import com.jfs.pms.domain.User;
import com.jfs.pms.dto.project.ProjectRequestDto;
import com.jfs.pms.dto.project.ProjectResponse;

import java.util.List;

public interface IProjectService {

    ProjectResponse addProject(User loggedInUser, ProjectRequestDto project);
    void updateProject(ProjectRequestDto project);
    boolean deleteProject(User loggedInUser, String publicId);
    List<ProjectResponse> getProjects(User loggedInUser);
    void updateProjectStatus(User loggeedInUser, String projectId, String status);
    boolean addMemberToProjectTeam(User loggeedInUser, String projectId, String memberId);
}
