package com.ridelo.management.notificationService;

import com.ridelo.management.model.EmailInfo;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendEmail(String messageType, String sendToMailId, EmailInfo emailInfo)
            throws MessagingException, UnsupportedEncodingException;
}
