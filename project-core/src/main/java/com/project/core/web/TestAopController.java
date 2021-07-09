package com.project.core.web;

import com.project.core.common.anaotation.SystemLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestAopController {

    @SystemLog(moduleId = "首页用户退出1111", description = "用户退出成功1111", opeType = SystemLog.OpeType.LOGOUT)
    @RequestMapping("/system/code/index")
    public String  test(){
        System.out.println("......................");
        return "client/index";
    }


}
