package com.jfs.pms.domain;

import com.jfs.pms.constants.IssueStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "jfs_pms_issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 999)
    private String description;
    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @ManyToOne
    @JoinColumn(name = "sprint_id", nullable = false)
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "assigner_id", nullable = false)
    private User assigner;

    public Long getId() {
        return id;
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

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }
}
