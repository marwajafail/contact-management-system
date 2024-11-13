
package com.example.contactApp.controller;

import com.example.contactApp.Dao.GenericDao;
import com.example.contactApp.Dto.ContactDto;
import com.example.contactApp.Dto.EventDto;
import com.example.contactApp.model.Event;
import com.example.contactApp.service.ContactService;
import com.example.contactApp.service.EventService;
import com.example.contactApp.service.GroupService;
import com.example.contactApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/event")
    public class EventController {

        private final EventService eventService;
        private final UserService userService;


        @Autowired
        public EventController(EventService eventService, UserService userService) {
            this.eventService = eventService;
            this.userService = userService;
        }

        // Get all Events record
        @GetMapping
        public ResponseEntity<GenericDao<List<EventDto>>> getAllEvents(@RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
            try {
                List<EventDto> eventDto = eventService.getAll(details);

                return !eventDto.isEmpty() ?
                        new ResponseEntity<>(new GenericDao<>(eventDto, null), HttpStatus.OK) :
                        new ResponseEntity<>(new GenericDao<>(null, List.of("No events found")), HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }


        // To read all events record by group Id
        @GetMapping("/{eventId}")
        public ResponseEntity<GenericDao<EventDto>> getEventById(@PathVariable(value = "eventId") Long eventId, @RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
            try {
                EventDto eventDto = eventService.getById(eventId, details);

                return eventDto != null ?
                        new ResponseEntity<>(new GenericDao<>(eventDto, null), HttpStatus.OK) :
                        new ResponseEntity<>(new GenericDao<>(null, List.of("Event with the id " + eventId + " was not found")), HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        // To create a new event record
        @PostMapping
        public ResponseEntity<GenericDao<EventDto>> createEvent(@RequestBody EventDto eventDto) {
            try {
                GenericDao<EventDto> genericDao = eventService.createEvent(eventDto);

                return genericDao.getErrors().isEmpty() ?
                        new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                        new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        // To edit event
        @PutMapping
        public ResponseEntity<GenericDao<EventDto>> editEvent(@RequestBody EventDto eventDto) {
            try {
                GenericDao<EventDto> genericDao = eventService.editEvent(eventDto);

                return genericDao.getErrors().isEmpty() ?
                        new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                        new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }


        // To delete event
        @DeleteMapping("/{eventId}")
        public ResponseEntity<GenericDao<Boolean>> deleteEvent(@PathVariable(value = "eventId") Long eventId) {
            try {
                GenericDao<Boolean> genericDao = eventService.deleteEvent(eventId);

                return genericDao.getErrors().isEmpty() ?
                        new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                        new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

    }
