package com.ridelo.management.notificationService;

import com.ridelo.management.model.EmailInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private  JavaMailSender mailSender;

    @Override
    public void sendEmail(String messageType, String sendToMailId, EmailInfo emailInfo)
            throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "admin@ridelo.com";
        String senderName = "RIDELO";
        String subject = emailInfo.getSubject();
        System.out.println(emailInfo.getBody());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(sendToMailId);
        helper.setSubject(subject);
        helper.setText(emailInfo.getBody(), true);
        mailSender.send(message);
        log.info("Email sent successfully to {} for {}",sendToMailId, messageType);
    }

}
