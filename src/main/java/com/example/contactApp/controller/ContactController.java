
package com.example.contactApp.controller;

import com.example.contactApp.Dao.GenericDao;
import com.example.contactApp.Dto.ContactDto;
import com.example.contactApp.model.Contact;
import com.example.contactApp.service.ContactService;
import com.example.contactApp.service.GroupService;
import com.example.contactApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;
    private final UserService userService;


    @Autowired
    public ContactController(ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    // Get all Contacts record
    @GetMapping
    public ResponseEntity<GenericDao<List<ContactDto>>> getAllContacts(@RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            List<ContactDto> contactDto = contactService.getAll(details);

            return !contactDto.isEmpty() ?
                    new ResponseEntity<>(new GenericDao<>(contactDto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("No contacts found")), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To read all contacts record by group Id
    @GetMapping("/{contactId}")
    public ResponseEntity<GenericDao<ContactDto>> getContactById(@PathVariable(value = "contactId") Long contactId, @RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            ContactDto contactDto = contactService.getById(contactId, details);

            return contactDto != null ?
                    new ResponseEntity<>(new GenericDao<>(contactDto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("Contact with the id " + contactId + " was not found")), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To create a new contact record
    @PostMapping
    public ResponseEntity<GenericDao<ContactDto>> createContact(@RequestBody ContactDto contactDto) {
        try {
            GenericDao<ContactDto> genericDao = contactService.createContact(contactDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To edit contact
    @PutMapping
    public ResponseEntity<GenericDao<ContactDto>> editContact(@RequestBody ContactDto contactDto) {
        try {
            GenericDao<ContactDto> genericDao = contactService.editContact(contactDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To delete contact
    @DeleteMapping("/{contactId}")
    public ResponseEntity<GenericDao<Boolean>> deleteContact(@PathVariable(value = "contactId") Long contactId) {
        try {
            GenericDao<Boolean> genericDao = contactService.deleteContact(contactId);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
