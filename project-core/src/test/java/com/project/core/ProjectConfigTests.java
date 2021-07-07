package com.project.core;

import com.project.core.web.config.ProjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(classes = com.project.ProjectCoreApplication.class)
@Slf4j
public class ProjectConfigTests {

    @Autowired
    private ProjectConfig projectConfig;

    @Test
    void testConfig() {
       // log.info(projectConfig.toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat.setLenient(true);

        try {
            Date date1 = dateFormat.parse("2012-02-02 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
