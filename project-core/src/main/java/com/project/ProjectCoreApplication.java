package com.project;

import com.project.core.web.config.ProjectConfig;
import com.project.core.web.config.ResourcePathConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class ProjectCoreApplication {

    private static ResourcePathConfig resourcePathConfig;

    private static ProjectConfig projectConfig;
    @Resource
    private Environment env;

    public ProjectCoreApplication(ResourcePathConfig resourcePathConfig, ProjectConfig projectConfig) {
        ProjectCoreApplication.resourcePathConfig = resourcePathConfig;
        ProjectCoreApplication.projectConfig = projectConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjectCoreApplication.class, args);
    }

    @Resource
    private void configureWebStaticVars(FreeMarkerViewResolver viewResolver) {
        if (viewResolver != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("ctx", env.getProperty("ctx"));
            map.put("project", projectConfig);
            viewResolver.setAttributesMap(map);
        }
    }


}
