package com.project.generator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@EqualsAndHashCode(callSuper = false)
public class GeneratorPageProperties extends GeneratorProperties {

    private String publicJsName;
    private String jsDirectory;

    private String targetDirectory;
    private String templatePath;
    private String templateJSFile;

    private String jsPackage;
    private String isHandleDuplicateSubmission;

}
