package org.shivam.script_writer.dto;

import org.shivam.script_writer.entity.UserCategory;

public record UserRequest(
        String name, String email, String password,Long  userCategoryId) {

}
