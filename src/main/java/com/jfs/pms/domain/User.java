package com.jfs.pms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "jfs_pms_users")
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    private UUID publicId;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String profileImageUrl;
    private String username;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Project> ownedProjects;

    @OneToMany(mappedBy = "coOwner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Project> coOwnedProjects;

    @ManyToMany(mappedBy = "teamMembers", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Project> projectsAsTeamMember;

    public Long getId() {
        return id;
    }

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

    public void setPassword(String password) {
        this.password = password;
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

    public Set<Project> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Set<Project> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    public Set<Project> getCoOwnedProjects() {
        return coOwnedProjects;
    }

    public void setCoOwnedProjects(Set<Project> coOwnedProjects) {
        this.coOwnedProjects = coOwnedProjects;
    }

    public Set<Project> getProjectsAsTeamMember() {
        return projectsAsTeamMember;
    }

    public void setProjectsAsTeamMember(Set<Project> projectsAsTeamMember) {
        this.projectsAsTeamMember = projectsAsTeamMember;
    }

    @Override
    public String getName() {
        return firstName + ", "+ lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", publicId=" + publicId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", username='" + username + '\'' +
                ", ownedProjects=" + ownedProjects +
                ", coOwnedProjects=" + coOwnedProjects +
                ", projectsAsTeamMember=" + projectsAsTeamMember +
                '}';
    }
}
