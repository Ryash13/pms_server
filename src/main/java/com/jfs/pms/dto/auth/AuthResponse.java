package com.jfs.pms.dto.auth;

import java.util.Set;
import java.util.UUID;

public class AuthResponse {

    private UUID publicId;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImageUrl;
    private String username;
    private Set<String> ownedProjects;
    private Set<String> coOwnedProjects;
    private Set<String> projectsAsTeamMember;

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Set<String> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    public Set<String> getCoOwnedProjects() {
        return coOwnedProjects;
    }

    public void setCoOwnedProjects(Set<String> coOwnedProjects) {
        this.coOwnedProjects = coOwnedProjects;
    }

    public Set<String> getProjectsAsTeamMember() {
        return projectsAsTeamMember;
    }

    public void setProjectsAsTeamMember(Set<String> projectsAsTeamMember) {
        this.projectsAsTeamMember = projectsAsTeamMember;
    }
}
