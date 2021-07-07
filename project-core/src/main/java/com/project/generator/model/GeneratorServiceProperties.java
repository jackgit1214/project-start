package com.project.generator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@EqualsAndHashCode(callSuper = false)
public class GeneratorServiceProperties extends GeneratorProperties {

    private boolean enabledUpdate = true;
    private boolean enabledDelete = true;
    private String implementationPackage;

    @Override
    public void afterPropertiesSet() throws Exception {
        //super.afterPropertiesSet();
        this.setProjectName(projectConfig.getConfigure().getProjectShortName().toLowerCase());
        this.setTargetPackage("com." + this.getProjectName() + ".service");
        this.setImplementationPackage(this.getTargetPackage() + ".impl");
    }
}
