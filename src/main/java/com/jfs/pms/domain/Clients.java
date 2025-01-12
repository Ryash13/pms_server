package com.jfs.pms.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity()
@Table(name = "jfs_pms_clients")
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @UuidGenerator
    private UUID publicId;

    private String clientName;
    private String description;
    private String profileImageUrl;

    @Column(unique = true, nullable = false, length = 75)
    private String clientDomain;

    @Column(nullable = false, updatable = false)
    private String createdBy;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Project> projects;

    public Long getId() {
        return id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getClientDomain() {
        return clientDomain;
    }

    public void setClientDomain(String clientDomain) {
        this.clientDomain = clientDomain;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
