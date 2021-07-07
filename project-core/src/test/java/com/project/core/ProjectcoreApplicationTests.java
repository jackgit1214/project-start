package com.project.core;

import com.project.core.web.config.ProjectConfig;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = com.project.ProjectCoreApplication.class)
class ProjectcoreApplicationTests {

    @Autowired
    private ProjectConfig projectConfig;

    @Test
    void contextLoads() {
    }

}
