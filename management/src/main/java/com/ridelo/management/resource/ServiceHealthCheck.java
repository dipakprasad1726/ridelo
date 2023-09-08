package com.ridelo.management.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ServiceHealthCheck {

    @GetMapping
    public ResponseEntity<Object> healthCheck(){
        return new ResponseEntity<>("Welcome to Ridelo service. Service is running fine..", HttpStatus.ACCEPTED);
    }

}
