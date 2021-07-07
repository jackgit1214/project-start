package com.project.generator.model;

import java.util.List;

import com.project.core.web.config.ProjectConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
public class GeneratorProperties implements InitializingBean {

    private String projectDirector;
    private String projectName = "demo";
    private String targetPackage = "com." + projectName + ".model";
    private String targetProject = "src/main/java";

    private String enableSubPackages = "true";
    private String trimStrings = "true";
    private String rootClass = "com.project.core.mybatis.model.BaseModel";
    private List<String> tableNames;
    private boolean generating;

    @Autowired
    protected ProjectConfig projectConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setProjectName(projectConfig.getConfigure().getProjectShortName().toLowerCase());
        this.setTargetPackage("com." + projectName + ".model");
        log.info(this.toString());

    }
}
