package com.example.contactApp.Dto;
import com.example.contactApp.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto implements Serializable {
    private Long id;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Boolean status;
    @JsonIgnore
    private String verificationCode;
    @JsonIgnore
    private boolean enabled;
    @JsonIgnore
    private String resetToken;
    @JsonIgnore
    private LocalDateTime tokenExpirationTime;
    private RoleDto role;
    private ProfileDto profile;
    private List<GroupDto> groups;
    private List<ContactDto> contacts;
    private List<EventDto> events;


    public UserDto(User user, boolean details) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.role = user.getRole() != null ? new RoleDto(user.getRole(), false) : null;

        // Only instantiate ProfileDto if details flag is true
        if (details) {
            this.profile = user.getProfile() != null ? new ProfileDto(user.getProfile(), false) : null;
        }

        this.verificationCode = user.getVerificationCode();
        this.enabled = user.isEnabled();
        this.resetToken = user.getResetToken();
        this.tokenExpirationTime = user.getTokenExpirationTime();

        if (details) {
            this.contacts = user.getContacts() != null ?
                    user.getContacts().stream().peek(contact -> contact.setUser(null)).map(contact -> new ContactDto(contact, false)).toList() :
                    new ArrayList<>();
            this.events = user.getEvents() != null ?
                    user.getEvents().stream().peek(event -> event.setUser(null)).map(event -> new EventDto(event, false)).toList() :
                    new ArrayList<>();
            this.groups = user.getGroups() != null ?
                    user.getGroups().stream().peek(group -> group.setUser(null)).map(group -> new GroupDto(group, false)).toList() :
                    new ArrayList<>();
        }
    }
    }
