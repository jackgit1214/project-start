package com.example.ssoclient2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @GetMapping(value = {"/aaa"})
    public String index(HttpServletRequest request,
                        Authentication authentication,
                        Model model) {
        return "aaa";
    }

    @GetMapping(value = {"/bbb"})
    public String bbb(HttpServletRequest request,
                        Authentication authentication,
                        Model model) {
        return "bbb";
    }
}
