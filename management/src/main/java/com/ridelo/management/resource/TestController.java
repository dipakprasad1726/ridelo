package com.ridelo.management.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TestController {

    @GetMapping("/hi")
    public String demo(){
        return "Hurrey... its working";
    }
}
