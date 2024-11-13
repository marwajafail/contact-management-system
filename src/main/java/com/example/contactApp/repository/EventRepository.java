package com.example.contactApp.repository;
import com.example.contactApp.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndUserId(Long id, Long userId);

    Optional<Event> findByTitleAndUserId(String title, Long userId);

    List<Event> findAllByUserId(Long userId);
}
