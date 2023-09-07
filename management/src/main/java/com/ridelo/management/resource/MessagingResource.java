package com.ridelo.management.resource;

import com.ridelo.management.driverOnboardingService.DriverServiceImpl;
import com.ridelo.management.notificationService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("email")
public class MessagingResource {

    @Autowired
    EmailService emailService;

    @Autowired
    DriverServiceImpl driverService;

    @GetMapping("/verification/{driverId}")
    public ResponseEntity<Object> emailVerification(@PathVariable("driverId") String driverId){
        return driverService.emailVerification(UUID.fromString(driverId));
    }



}
