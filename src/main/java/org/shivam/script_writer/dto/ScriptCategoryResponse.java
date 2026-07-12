package org.shivam.script_writer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScriptCategoryResponse(
        Long id, String name, LocalDateTime createdAt) {

}
