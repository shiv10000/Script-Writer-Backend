package org.shivam.script_writer.controller;

import org.shivam.script_writer.dto.UserResponse;
import org.shivam.script_writer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}/promote")
    public ResponseEntity<UserResponse> promoteToAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(userService.promoteToAdmin(id));
    }
}
