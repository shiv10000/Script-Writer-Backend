package org.shivam.script_writer.controller;


import org.shivam.script_writer.dto.UserCategoryRequest;
import org.shivam.script_writer.dto.UserCategoryResponse;
import org.shivam.script_writer.service.UserCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserCategoryController {

    private final UserCategoryService userCategoryService;

    public UserCategoryController(UserCategoryService userCategoryService) {
        this.userCategoryService = userCategoryService;
    }

    @PostMapping("/category")
    public ResponseEntity<UserCategoryResponse> setUserCategory( @RequestBody UserCategoryRequest userCategoryRequest){
        return  ResponseEntity.ok(userCategoryService.createUserCategory(userCategoryRequest));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<UserCategoryResponse> updateUserCategory(@PathVariable Long id, @RequestBody UserCategoryRequest userCategoryRequest){
        return ResponseEntity.ok(userCategoryService.updateUserCategory(id,userCategoryRequest));
    }

    @GetMapping("/category")
    public ResponseEntity<List<UserCategoryResponse>> getALLUserCategory(){
        return ResponseEntity.ok(userCategoryService.getAllUserCategory());
    }

    @DeleteMapping("/category")
    public ResponseEntity<UserCategoryResponse> deleteUserCategory( @RequestBody UserCategoryRequest userCategoryRequest){
        return  ResponseEntity.ok(userCategoryService.deleteUserCategory(userCategoryRequest));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<UserCategoryResponse> getUserCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(userCategoryService.getUserCategoryById(id));
    }

}
