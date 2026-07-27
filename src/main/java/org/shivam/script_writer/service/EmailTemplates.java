package org.shivam.script_writer.service;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplates {

    public String verificationOtp(String name, String code) {
        return """
            <div style="font-family: Georgia, serif; color: #202124; max-width: 600px;">
                <p>Dear %s,</p>
                <p>Please use this OTP to verify your account:
                   <b style="background-color: #fff2cc; padding: 2px 6px;">%s</b>
                </p>
                <p>This code expires in 5 minutes. Do not share this OTP with anyone.</p>
                %s
            </div>
            """.formatted(name, code, footer());
    }

    public String welcomeEmail(String name) {
        return """
            <div style="font-family: Georgia, serif; color: #202124; max-width: 600px;">
                <p>Welcome aboard, %s!</p>
                <p>Your account is now verified and ready to use.</p>
                %s
            </div>
            """.formatted(name, footer());
    }

    private String footer() {
        return """
            <p><b>Warm Regards,</b><br/>Script Writer Team</p>
            <hr style="margin-top: 24px; border: none; border-top: 1px solid #ddd;"/>
            <p style="font-size: 12px; color: #888;">This is a system generated mail. Please do not reply.</p>
            """;
    }
}
