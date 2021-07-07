package com;

import com.project.core.web.config.ProjectConfig;
import com.project.core.web.config.ResourcePathConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(exclude = {
        FlywayAutoConfiguration.class
})
public class ProjectSampleApplication {
    private static ProjectConfig projectConfig;
    private static ResourcePathConfig resourcePathConfig;
    @Resource
    private Environment env;

    public ProjectSampleApplication(ResourcePathConfig resourcePathConfig, ProjectConfig projectConfig) {
        ProjectSampleApplication.resourcePathConfig = resourcePathConfig;
        ProjectSampleApplication.projectConfig = projectConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjectSampleApplication.class, args);
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


    /**
     * 配置csrf 过程器
     *
     * @return
     */
//    @Bean
//    public FilterRegistrationBean<CsrfFilter> registerCsrf() {
//
//        FilterRegistrationBean<CsrfFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new CsrfFilter(new HttpSessionCsrfTokenRepository()));
//        registration.addUrlPatterns("/*");
//        registration.setName("csrfFilter");
//        return registration;
//    }


}
