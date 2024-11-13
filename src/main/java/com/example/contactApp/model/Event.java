package com.example.contactApp.model;
import com.example.contactApp.Dto.ContactDto;
import com.example.contactApp.Dto.EventDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId",nullable = false)
    private Long id;
    @Column(name = "name",nullable = false)
    private String title;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "eventDate",nullable = false)
    private LocalDateTime date;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    @ToString.Exclude
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId")
    @ToString.Exclude
    @JsonBackReference
    private Group group;


    public Event(EventDto dto) {
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.date = dto.getDate();
        this.group = dto.getGroup() != null ?
                new Group(dto.getGroup()) :
                null;
        this.user = dto.getUser() != null ?
                new User(dto.getUser()) :
                null;
    }

}

