package com.project.ssoclient2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value = {"/index"})
    public String index() {
        return "index";
    }

    @GetMapping(value = {"/vvvv"})
    public String indexvvvv() {
        return "index";
    }

}
