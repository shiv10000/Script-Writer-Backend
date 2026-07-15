package org.shivam.script_writer.controller;


import org.shivam.script_writer.dto.UserRequest;
import org.shivam.script_writer.dto.UserResponse;
import org.shivam.script_writer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {

        UserResponse userResponse = userService.createUser(userRequest);

        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {

        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));

    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

}

