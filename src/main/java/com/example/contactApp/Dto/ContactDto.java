package com.example.contactApp.Dto;

import com.example.contactApp.model.Contact;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactDto implements Serializable {
    private Long id;
    private String name;
    private  String email;
    private String  phone;
    private GroupDto group;
    private UserDto user;


    public ContactDto(Contact contact, boolean details) {
        this.id = contact.getId();
        this.name = contact.getName();
        this.email = contact.getEmail();
        this.phone = contact.getPhone();
        this.group = contact.getGroup() != null ?
                new GroupDto(contact.getGroup(), false) :
                null;
        this.user = contact.getUser() != null ?
                new UserDto(contact.getUser(), false) :
                null;

    }
}