package com.helloworld.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
//@RequestMapping("/hi")
public class HelloWorldController 
{ @RequestMapping("/")
public String hello() 
{
return "<h1> Congratulations. You have successfully deployed the sample Spring Boot Application. </h1>";
}
}
