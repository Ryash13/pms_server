package com.jfs.pms.utility;

import com.jfs.pms.constants.ProjectStatus;
import com.jfs.pms.domain.Project;
import com.jfs.pms.domain.User;
import com.jfs.pms.dto.auth.RegisterDto;
import com.jfs.pms.dto.auth.AuthResponse;
import com.jfs.pms.dto.project.ProjectRequestDto;
import com.jfs.pms.dto.project.ProjectResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Mapper {

    public static User fromRegisterToUser(RegisterDto register) {
        var user = new User();
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setEmail(register.getEmail());
        user.setPassword(register.getPassword());
        return user;
    }

    public static AuthResponse fromUserToAuthResponse(User user) {
        var userDto = new AuthResponse();
        userDto.setPublicId(user.getPublicId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setProfileImageUrl(user.getProfileImageUrl());
        userDto.setOwnedProjects(
                user.getOwnedProjects()
                        .stream()
                        .map(projects -> projects.getPublicId().toString())
                        .collect(Collectors.toSet())
        );
        userDto.setCoOwnedProjects(
                user.getCoOwnedProjects()
                        .stream()
                        .map(projects -> projects.getPublicId().toString())
                        .collect(Collectors.toSet())
        );
        userDto.setProjectsAsTeamMember(
                user.getProjectsAsTeamMember()
                        .stream()
                        .map(projects -> projects.getPublicId().toString())
                        .collect(Collectors.toSet())
        );
        return userDto;
    }

    public static Project fromProjectRequestToProject(ProjectRequestDto requestDto) {
        var project = new Project();
        project.setName(requestDto.getName());
        project.setDescription(requestDto.getDescription());
        project.setStartDate(requestDto.getStartDate());
        project.setEndDate(requestDto.getEndDate());
        project.setSlug(requestDto.getSlug());
        project.setStatus(ProjectStatus.valueOf(requestDto.getStatus()));
        return project;
    }

    public static ProjectResponse fromProjectToProjectResponse(Project project) {
        var projectDto = new ProjectResponse();
        projectDto.setPublicId(project.getPublicId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setEndDate(project.getEndDate());
        projectDto.setSlug(project.getSlug());
        projectDto.setStatus(project.getStatus());
        projectDto.setOwnerId(project.getOwner().getPublicId().toString());
        projectDto.setTeamMembers(Optional.of(project.getTeamMembers()
                .stream()
                .map(User::getPublicId)
                .map(Object::toString)
                .collect(Collectors.toSet()))
                .orElse(null));
        projectDto.setCoOwnerId(Optional.ofNullable(project.getCoOwner())
                .map(User::getPublicId)
                .map(Object::toString)
                .orElse(null));
        return projectDto;
    }

    public static List<ProjectResponse> fromProjectsToProjectResponse(List<Project> projects) {
        return projects.stream()
                .map(Mapper::fromProjectToProjectResponse)
                .collect(Collectors.toList());
    }
}