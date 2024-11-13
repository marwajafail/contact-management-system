package com.example.contactApp.repository;

import com.example.contactApp.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByIdAndUserId(Long id, Long userId);

    Optional<Contact> findByNameAndUserId(String name, Long userId);

    List<Contact> findAllByUserId(Long userId);
}
