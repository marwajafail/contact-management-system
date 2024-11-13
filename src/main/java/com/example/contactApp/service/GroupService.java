package com.example.contactApp.service;

import com.example.contactApp.Dao.GenericDao;
import com.example.contactApp.Dto.GroupDto;
import com.example.contactApp.kafka_producer.KafkaProducer;
import com.example.contactApp.model.Group;
import com.example.contactApp.repository.GroupRepository;
import com.example.contactApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer; // Kafka producer

    @Autowired
    public GroupService( GroupRepository groupRepository, UserRepository userRepository,KafkaProducer kafkaProducer) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
    }
// Retrieves a group by its ID.
    public GroupDto getById(Long groupId, Boolean details) {
        return groupRepository.findByIdAndUserIdOrUserId(groupId, UserService.getCurrentLoggedInUser().getId(), 1L).map(group -> new GroupDto(group, details)).orElse(null);
    }
//Retrieves a group by its name.
    public GroupDto getByName(String name, Boolean details) {
        return groupRepository.findByNameAndUserIdOrUserId(name, UserService.getCurrentLoggedInUser().getId(), 1L).map(group -> new GroupDto(group, details)).orElse(null);
    }

    public List<GroupDto> getAll(Boolean details) {
        return groupRepository.findAllByUserIdOrUserId(UserService.getCurrentLoggedInUser().getId(), 1L).stream().map(group -> new GroupDto(group, details)).toList();
    }
//Creates a new group based on the provided GroupDto.
    public GenericDao<GroupDto> createGroup(GroupDto dto) {
        GenericDao<GroupDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();

        if (dto.getName() == null || dto.getName().isBlank()) {
            errors.add("Group name cannot be empty");
        }

        if (errors.isEmpty()) {
            Long uid = UserService.getCurrentLoggedInUser().getId();
            Optional<Group> retrievedGroup = groupRepository.findByNameAndUserId(dto.getName(), uid);

            if (retrievedGroup.isEmpty()) {
                Group group = new Group(dto);
                group.setUser(userRepository.findById(uid).orElse(null));
                returnDao.setObject(new GroupDto(group, false));

                // Send Kafka message for new group creation
                kafkaProducer.sendNewGroupDetails(group);
            } else {
                errors.add("Group already exists");
            }
        }

        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }

//Edits an existing group.
    public GenericDao<GroupDto> editGroup(GroupDto dto) {
        GenericDao<GroupDto> returnDao = new GenericDao<>();

        List<String> errors = new ArrayList<>();

        if (dto.getId() == null) {
            errors.add("Group ID cannot be empty");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            errors.add("Group name cannot be empty");
        }

        if (errors.isEmpty()) {
            Optional<Group> retrievedGroup = groupRepository.findByIdAndUserId(dto.getId(), UserService.getCurrentLoggedInUser().getId());

            if (retrievedGroup.isPresent()) {
                if (retrievedGroup.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {

                    retrievedGroup.get().setName(dto.getName());
                    Group savedGroup = groupRepository.save(retrievedGroup.get());

                    returnDao.setObject(new GroupDto(savedGroup, false));
                } else {
                    errors.add("Cannot edit group of another user");
                }
            } else {
                errors.add("Group does not exist");
            }
        }

        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }
//Deletes a group by its ID.
    public GenericDao<Boolean> deleteGroup(Long groupId) {
        List<String> errors = new ArrayList<>();
        Optional<Group> retrievedGroup = groupRepository.findByIdAndUserId(groupId, UserService.getCurrentLoggedInUser().getId());
        if (retrievedGroup.isPresent()) {
            if (retrievedGroup.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                groupRepository.deleteById(groupId);
            } else {
                errors.add("Cannot delete group of another user");
            }
            return errors.isEmpty() ?
                    new GenericDao<>(true, errors) :
                    new GenericDao<>(false, errors);
        } else {
            errors.add("Group does not exist");
            return new GenericDao<>(false, errors);
        }
    }
}





