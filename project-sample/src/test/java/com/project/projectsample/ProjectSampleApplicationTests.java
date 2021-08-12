package com.project.projectsample;

import com.project.core.common.util.ApplicationContextUtil;
import com.project.sso.handler.CustomLogoutHandler;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectSampleApplicationTests {

    @Test
    void contextLoads() {
        CustomLogoutHandler test = ApplicationContextUtil.getBean(CustomLogoutHandler.class);

        //test.logout(null,null,null);

        CustomLogoutHandler test1 = new CustomLogoutHandler();

        test1.logout(null,null,null);
    }

}
