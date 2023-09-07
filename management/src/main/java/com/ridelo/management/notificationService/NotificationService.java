package com.ridelo.management.notificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMailMessage(
            final SimpleMailMessage simpleMailMessage) {

        this.mailSender.send(simpleMailMessage);
    }
}
