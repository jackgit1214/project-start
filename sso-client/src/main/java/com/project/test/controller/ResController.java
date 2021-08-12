package com.project.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/res")
public class ResController {

    @GetMapping("/res1")
    public String getRes1(){
        return "res1";
    }

    @GetMapping("/res2")
    public String getRes2(){
        return "res2";
    }
}
