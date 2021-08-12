package com.project.generator.model;

import com.project.core.common.GeneratorCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@Conditional({GeneratorCondition.class})
public class GeneratorPageProperties extends GeneratorProperties {

    private String publicJsName;
    private String jsDirectory;
    private String pageVarName;
    private String targetDirectory="src/main/resources/templates";
    private String templatePath;
    private String templateJSFile;

    private String jsPackage;
    private String isHandleDuplicateSubmission;

}
