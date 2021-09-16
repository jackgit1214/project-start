package com.project.ssoclient2.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ApiController {

    @GetMapping("/api/v1/user")
    public String testCors(){

        return "my user";
    }
}
