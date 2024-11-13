
package com.example.contactApp.service;

import com.example.contactApp.Dao.GenericDao;
import com.example.contactApp.Dto.EventDto;
import com.example.contactApp.model.Event;
import com.example.contactApp.model.Group;
import com.example.contactApp.repository.EventRepository;
import com.example.contactApp.repository.GroupRepository;
import com.example.contactApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    @Autowired
    public EventService(EventRepository eventRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }
//Retrieves an event by its ID.
    public EventDto getById(Long eventId, Boolean details) {
        return eventRepository.findByIdAndUserId(eventId, UserService.getCurrentLoggedInUser().getId()).map(event -> new EventDto(event, details)).orElse(null);
    }
//Retrieves all events for the current user.
    public List<EventDto> getAll(Boolean details) {
        return eventRepository.findAllByUserId(UserService.getCurrentLoggedInUser().getId()).stream().map(event -> new EventDto(event, details)).toList();
    }
//Creates a new event based on the provided EventDto.
    public GenericDao<EventDto> createEvent(EventDto dto) {
        GenericDao<EventDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();

        if (dto.getTitle() == null) {
            errors.add("Event Title cannot be empty");
        }

        if (dto.getDescription() == null) {
            errors.add("Description cannot be empty");
        }
        if (dto.getDate() == null) {
            errors.add("Date cannot be empty");
        }

        Optional<Group> group = Optional.empty();
        if (dto.getGroup().getId() == null) {
            errors.add("Group ID cannot be empty");
        } else {
            group = groupRepository.findByIdAndUserId(dto.getGroup().getId(), UserService.getCurrentLoggedInUser().getId());
            if (group.isEmpty()) {
                errors.add("Group does not exists");
            }
        }

        if (errors.isEmpty()) {
            Long uid = UserService.getCurrentLoggedInUser().getId();
            Optional<Event> retrievedEvent = eventRepository.findByTitleAndUserId(dto.getTitle(), uid);
            if (retrievedEvent.isEmpty() || (retrievedEvent.get().getTitle().equals(dto.getTitle()) && retrievedEvent.get().getDate() != dto.getDate())) {
                Event event = new Event(dto);
                event.setGroup(group.get());
                event.setUser(userRepository.findById(uid).get());
                Event savedEvent = eventRepository.save(event);
                returnDao.setObject(new EventDto(savedEvent, false));
            } else {
                errors.add("Event already exists");
            }
        }
        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }
        return returnDao;
    }

//Edits an existing event.
    public GenericDao<EventDto> editEvent(EventDto dto) {
        GenericDao<EventDto> returnDao = new GenericDao<>();

        List<String> errors = new ArrayList<>();

        if (dto.getId() == null) {
            errors.add("Event ID cannot be empty");
        }

        if (dto.getTitle()== null) {
            errors.add("Event title cannot be empty");
        }

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            errors.add("Event title cannot be empty");
        }

        if (dto.getDescription() == null) {
            errors.add("Description cannot be empty");
        }

        if (dto.getDate() == null) {
            errors.add("Date cannot be empty");
        }

        if (errors.isEmpty()) {
            Optional<Event> retrievedEvent = eventRepository.findByIdAndUserId(dto.getId(), UserService.getCurrentLoggedInUser().getId());

            if (retrievedEvent.isPresent()) {
                if (retrievedEvent.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                    retrievedEvent.get().setTitle(dto.getTitle());
                    retrievedEvent.get().setDescription(dto.getDescription());
                    retrievedEvent.get().setDate(dto.getDate());

                    Event savedEvent = eventRepository.save(retrievedEvent.get());

                    returnDao.setObject(new EventDto(savedEvent, false));
                } else {
                    errors.add("Cannot edit event of another user");
                }
            } else {
                errors.add("Event does not exist");
            }
        }

        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }
//Deletes an event by its ID
    public GenericDao<Boolean> deleteEvent(Long eventId) {
        Optional<Event> retrievedEvent = eventRepository.findByIdAndUserId(eventId, UserService.getCurrentLoggedInUser().getId());
        List<String> errors = new ArrayList<>();
        if (retrievedEvent.isPresent()) {
            if (retrievedEvent.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                eventRepository.deleteById(eventId);
            } else {
                errors.add("Cannot delete event of another user");
            }
            return errors.isEmpty() ?
                    new GenericDao<>(true, errors) :
                    new GenericDao<>(false, errors);
        } else {
            errors.add("Contact does not exist");
            return new GenericDao<>(false, errors);
        }
    }

}