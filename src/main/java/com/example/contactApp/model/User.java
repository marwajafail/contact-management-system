package com.example.contactApp.model;

import com.example.contactApp.Dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    //Used if admin deactivates account
    @Column(name = "status")
    private Boolean status;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    //Used for user verification of account
    @Column(name = "isEnabled")
    private boolean enabled;

    @Column(name = "resetToken")
    private String resetToken;

    @Column(name = "tokenExpirationTime")
    private LocalDateTime tokenExpirationTime;

    @ManyToOne
    @JoinColumn(name = "\"roleId\"")
    @ToString.Exclude
    private Role role;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "\"profileId\"")
    @ToString.Exclude
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Group> groups;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Contact> contacts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Event> events;

    public User(UserDto dto) {
        this.id = dto.getId();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.status = dto.getStatus();
        this.role = dto.getRole() != null ?
                new Role(dto.getRole()) :
                null;
        this.profile = dto.getProfile() != null ?
                new Profile(dto.getProfile()) :
                null;
        this.contacts = dto.getContacts() != null ?
                dto.getContacts().stream().map(Contact::new).toList() :
                null;
        this.events = dto.getEvents() != null ?
                dto.getEvents().stream().map(Event::new).toList() :
                null;
        this.groups = dto.getGroups() != null ?
                dto.getGroups().stream().map(Group::new).toList() :
                null;
        this.verificationCode = dto.getVerificationCode();
        this.enabled = dto.isEnabled();
        this.resetToken = dto.getResetToken();
        this.tokenExpirationTime = dto.getTokenExpirationTime();
    }
}