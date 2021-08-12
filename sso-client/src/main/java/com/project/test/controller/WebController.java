package com.project.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    @GetMapping(value = {"/"})
    public String index() {
        String test="测试输出内容1111。";
        String test1="lasjdf;asdfjasdfasdfasdf";
        System.out.println(test1);
        return "index";
    }

    @GetMapping(value = {"/vvvv"})
    @ResponseBody
    public String indexvvvv() {
        return "index";
    }

}
