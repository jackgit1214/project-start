package com.project.core.web.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "project.path")
public class ResourcePathConfig {

    private String resourceUrl;
    private String dataResourceUrl;
    private String imageResourceUrl;
    private String otherResourceUrl;
    private String typeResourceUrl;
    private String dataDefaultImageResourceUrl;

}
