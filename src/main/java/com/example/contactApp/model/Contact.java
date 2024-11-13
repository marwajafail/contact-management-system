package com.example.contactApp.model;
import com.example.contactApp.Dto.ContactDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity


public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false)
    private Long id;
    @Column( nullable = false)
    private String name;
    @Column( nullable = false)
    private String email;
    @Column( nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"groupId\"")
    @ToString.Exclude
    @JsonBackReference
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    @ToString.Exclude
    private User user;


    public Contact(ContactDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        this.group = dto.getGroup() != null ?
                new Group(dto.getGroup()) :
                null;
        this.user = dto.getUser() != null ?
                new User(dto.getUser()) :
                null;
    }



}
