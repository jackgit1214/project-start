package com.project.config;

import org.apache.catalina.filters.RequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;


@Configuration
public class SwaggerUiConfiguration implements WebMvcConfigurer {

    @Value("${server.path.globalPrefix}")
    private String pathGlobalPrefix;

    @Value("${server.path.packageName}")
    private String packageName;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.
            addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
        registry.addRedirectViewController("apihelp","/swagger-ui/");
    }



    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //WebMvcConfigurer.super.configurePathMatch(configurer);
        configurer
                .addPathPrefix(pathGlobalPrefix,c -> {
                    boolean isAddPrefix = c.getPackage().getName().startsWith(packageName);
                    return isAddPrefix && c.isAnnotationPresent(RestController.class);
                });
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        //.allowedOrigins("*", "http://localhost:3000","http://localhost:9001") //
                        // 允许跨域的域名
                        .allowedOriginPatterns("*") // 允许所有域
                        .allowedMethods("*") // 允许任何方法（post、get等）
                        .allowedHeaders("*") // 允许任何请求头
                        .allowCredentials(true) // 允许证书、cookie
                        .exposedHeaders(HttpHeaders.SET_COOKIE)
                        .maxAge(3600L); // maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
            }
        };
    }


}
