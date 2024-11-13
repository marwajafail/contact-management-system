package com.example.contactApp.repository;

import com.example.contactApp.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByIdAndUserIdOrUserId(Long id, Long userId, Long adminId);

    Optional<Group> findByIdAndUserId(Long id, Long userId);

    Optional<Group> findByNameAndUserId(String groupName, Long userId);

    Optional<Group> findByNameAndUserIdOrUserId(String groupName, Long userId, Long adminId);

    List<Group> findAllByUserIdOrUserId(Long userId, Long adminId);

    // Find group by name (no user or admin constraints)
    Optional<Group> findByName(String groupName);


}
