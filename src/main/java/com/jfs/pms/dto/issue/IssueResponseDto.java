package com.jfs.pms.dto.issue;

import com.jfs.pms.constants.enums.IssuePriority;
import com.jfs.pms.constants.enums.IssueStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class IssueResponseDto {

    private Long id;
    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private String tags;
    private int points;
    private Map<String, String> sprint;
    private Map<String, String> assignee;
    private Map<String, String> assigner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Map<String, String> getSprint() {
        return sprint;
    }

    public void setSprint(Map<String, String> sprint) {
        this.sprint = sprint;
    }

    public Map<String, String> getAssignee() {
        return assignee;
    }

    public void setAssignee(Map<String, String> assignee) {
        this.assignee = assignee;
    }

    public Map<String, String> getAssigner() {
        return assigner;
    }

    public void setAssigner(Map<String, String> assigner) {
        this.assigner = assigner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
