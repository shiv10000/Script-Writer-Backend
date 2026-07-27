package org.shivam.script_writer.dto;

public record VerificationRequiredResponse(
        String code,
        String message,
        String email
) {
}
