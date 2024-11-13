package com.example.contactApp.model;
import com.example.contactApp.Dto.ContactDto;
import com.example.contactApp.Dto.GroupDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "groups")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groupId")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Contact> contacts  = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Event> events  = new ArrayList<>();


    public Group(GroupDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.user = dto.getUser() != null ?
                new User(dto.getUser()) :
                null;
        this.contacts = dto.getContacts() != null ?
                dto.getContacts().stream().map(Contact::new).toList() :new ArrayList<>();

        this.events = dto.getEvents() != null ?
                dto.getEvents().stream().map(Event::new).toList() :new ArrayList<>();



    }




}
