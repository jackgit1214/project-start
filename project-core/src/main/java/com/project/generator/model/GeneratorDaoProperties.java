package com.project.generator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@EqualsAndHashCode(callSuper = false)
public class GeneratorDaoProperties extends GeneratorProperties {

    private String mapperTargetPackage;
    private String mapperTargetProject;
    private String configurationType = "XMLMAPPER";


    @Override
    public void afterPropertiesSet() throws Exception {
        //super.afterPropertiesSet();
        this.setProjectName(projectConfig.getConfigure().getProjectShortName().toLowerCase());
        this.setTargetPackage("com." + this.getProjectName() + ".dao");
        this.setMapperTargetPackage("mapper");
        this.setMapperTargetProject("src/main/resources");
    }

}