package org.shivam.script_writer;


import org.junit.jupiter.api.Test;
import org.shivam.script_writer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailTest {

    @Autowired
    private EmailService emailService;


    @Test
    void testEmailSend(){
        emailService.sendHtmlEmail("shivam.jarvis1000@gmail.com", "Testing", "kaise hoo app");
    }
}
