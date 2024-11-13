package com.example.contactApp.Dto;

import com.example.contactApp.model.Group;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDto implements Serializable {
    private Long id;
    private String name;
    private UserDto user;
    private List<ContactDto> contacts;
    private List<EventDto> events;

    public GroupDto(Group group, boolean details) {
        this.id = group.getId();
        this.name = group.getName();
        this.user = group.getUser() != null ?
                new UserDto(group.getUser(), false) :
                null;
        if (details) {
            this.contacts = group.getContacts() != null ?
                    group.getContacts().stream().peek(contact -> contact.setGroup(null)).map(contact -> new ContactDto(contact, false)).toList() :
                    new ArrayList<>();
            this.events = group.getEvents() != null ?
                    group.getEvents().stream().peek(event -> event.setGroup(null)).map(event -> new EventDto(event, false)).toList() :
                    new ArrayList<>();

        }
    }
}


