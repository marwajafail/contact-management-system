package com.example.contactApp.controller;

import com.example.contactApp.Dao.GenericDao;
import com.example.contactApp.Dto.GroupDto;
import com.example.contactApp.service.GroupService;
import com.example.contactApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;



    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;

    }
    // To read all group records
    @GetMapping
    public ResponseEntity<GenericDao<List<GroupDto>>> getAllGroups(@RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            List<GroupDto> dto = groupService.getAll(details);
            return !dto.isEmpty() ?
                    new ResponseEntity<>(new GenericDao<>(dto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("No groups found")), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //To create a new group record
    @PostMapping
    public ResponseEntity<GenericDao<GroupDto>> createGroup(@RequestBody GroupDto groupDto) {
        try {
            GenericDao<GroupDto> genericDao = groupService.createGroup(groupDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To read a group record
    @GetMapping("/{groupId}")
    public ResponseEntity<GenericDao<GroupDto>> getGroupById(@PathVariable(value = "groupId") Long groupId, @RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            GroupDto groupDto = groupService.getById(groupId, details);

            return groupDto != null ?
                    new ResponseEntity<>(new GenericDao<>(groupDto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("Group with the id " + groupId + " was not found")), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To read a group record by name
    @GetMapping("/name/{groupName}")
    public ResponseEntity<GenericDao<GroupDto>> getGroupByName(@PathVariable(value = "groupName") String groupName, @RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            GroupDto groupDto = groupService.getByName(groupName, details);

            return groupDto != null ?
                    new ResponseEntity<>(new GenericDao<>(groupDto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("Group with the name " + groupName + " was not found")), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // To update group record
    @PutMapping
    public ResponseEntity<GenericDao<GroupDto>> editGroup(@RequestBody GroupDto groupDto) {
        try {
            GenericDao<GroupDto> genericDao = groupService.editGroup(groupDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.OK) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To delete a group record
    @DeleteMapping("/{groupId}")
    public ResponseEntity<GenericDao<Boolean>> deleteGroup(@PathVariable(value = "groupId") Long groupId) {
        try {
            GenericDao<Boolean> genericDao = groupService.deleteGroup(groupId);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.OK) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
