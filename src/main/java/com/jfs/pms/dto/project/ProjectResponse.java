package com.jfs.pms.dto.project;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import com.jfs.pms.constants.ProjectStatus;

public class ProjectResponse {

    private String name;
    private String description;
    private String slug;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID publicId;
    private ProjectStatus status;
    private String ownerId;
    private String coOwnerId;
    private Set<String> teamMembers;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCoOwnerId() {
        return coOwnerId;
    }

    public void setCoOwnerId(String coOwnerId) {
        this.coOwnerId = coOwnerId;
    }

    public Set<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(Set<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

}
