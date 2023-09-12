package com.ridelo.management.notificationService;

import com.ridelo.management.ManagementApplication;
import com.ridelo.management.model.EmailInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@SpringBootTest(classes = ManagementApplication.class)
public class NotificationServiceTest {


    @Autowired
    private EmailService emailService ;
    @Test
    public void sendEmail() throws MessagingException, UnsupportedEncodingException {
        EmailInfo emailInfo = EmailDataConstants.emailInfoMap.get
                (EmailDataConstants.MessageType.EMAIL_VERIFICATION.toString());
        emailInfo.setBody(String.format(emailInfo.getBody(),"Test User",
                "987654321"));
        emailService.sendEmail(EmailDataConstants.MessageType.EMAIL_VERIFICATION.toString(),
                "dipakprasad53@gmail.com",emailInfo);
    }
}
