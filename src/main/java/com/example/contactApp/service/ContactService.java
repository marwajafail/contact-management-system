
package com.example.contactApp.service;

import com.example.contactApp.Dao.GenericDao;
import com.example.contactApp.Dto.ContactDto;
import com.example.contactApp.model.Contact;
import com.example.contactApp.model.Group;
import com.example.contactApp.repository.ContactRepository;
import com.example.contactApp.repository.GroupRepository;
import com.example.contactApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    @Autowired
    public ContactService(ContactRepository contactRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public ContactDto getById(Long contactId, Boolean details) {
        return contactRepository.findByIdAndUserId(contactId, UserService.getCurrentLoggedInUser().getId()).map(contact -> new ContactDto(contact, details)).orElse(null);
    }

    public List<ContactDto> getAll(Boolean details) {
        return contactRepository.findAllByUserId(UserService.getCurrentLoggedInUser().getId()).stream().map(contact -> new ContactDto(contact, details)).toList();
    }

    public GenericDao<ContactDto> createContact(ContactDto dto) {
        GenericDao<ContactDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();

        if (dto.getName() == null) {
            errors.add("Contact name cannot be empty");
        }

        if (dto.getEmail() == null) {
            errors.add("Email cannot be empty");
        }
        if (dto.getPhone() == null) {
            errors.add("Phone number cannot be empty");
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
            Optional<Contact> retrievedContact = contactRepository.findByNameAndUserId(dto.getName(), uid);
            if (retrievedContact.isEmpty() || (retrievedContact.get().getName().equals(dto.getName()) && retrievedContact.get().getEmail() != dto.getEmail())) {
                Contact contact = new Contact(dto);
                contact.setGroup(group.get());
                contact.setUser(userRepository.findById(uid).get());
                Contact savedContact = contactRepository.save(contact);
                returnDao.setObject(new ContactDto(savedContact, false));
            } else {
                errors.add("Group already exists");
            }
        }
        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }
        return returnDao;
    }


    public GenericDao<ContactDto> editContact(ContactDto dto) {
        GenericDao<ContactDto> returnDao = new GenericDao<>();

        List<String> errors = new ArrayList<>();

        if (dto.getId() == null) {
            errors.add("Contact ID cannot be empty");
        }

        if (dto.getName() == null) {
            errors.add("Contact name cannot be empty");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            errors.add("Contact name cannot be empty");
        }

        if (dto.getEmail() == null) {
            errors.add("Email cannot be empty");
        }

        if (dto.getPhone() == null) {
            errors.add("Phone number cannot be empty");
        }

        if (errors.isEmpty()) {
            Optional<Contact> retrievedContact = contactRepository.findByIdAndUserId(dto.getId(), UserService.getCurrentLoggedInUser().getId());

            if (retrievedContact.isPresent()) {
                if (retrievedContact.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                    retrievedContact.get().setName(dto.getName());
                    retrievedContact.get().setEmail(dto.getEmail());
                    retrievedContact.get().setPhone(dto.getPhone());

                    Contact savedContact = contactRepository.save(retrievedContact.get());

                    returnDao.setObject(new ContactDto(savedContact, false));
                } else {
                    errors.add("Cannot edit contact of another user");
                }
            } else {
                errors.add("Contact does not exist");
            }
        }

        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }

    public GenericDao<Boolean> deleteContact(Long contactId) {
        Optional<Contact> retrievedContact = contactRepository.findByIdAndUserId(contactId, UserService.getCurrentLoggedInUser().getId());
        List<String> errors = new ArrayList<>();
        if (retrievedContact.isPresent()) {
            if (retrievedContact.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                contactRepository.deleteById(contactId);
            } else {
                errors.add("Cannot delete contact of another user");
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