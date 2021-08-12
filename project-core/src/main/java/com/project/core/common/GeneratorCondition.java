package com.project.core.common;

import com.project.core.web.config.ProjectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class GeneratorCondition implements Condition {

//    @Autowired
//    private ProjectConfig projectConfig;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Environment environment = context.getEnvironment();
        boolean isGen = Boolean.parseBoolean(environment.getProperty("project.configure.code-tool"));
        return isGen;
    }
}
