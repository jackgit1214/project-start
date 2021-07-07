package com.project.core.web.config;

import java.util.List;

import javax.servlet.MultipartConfigElement;

import com.project.core.common.binding.FormBeanAttrArgumentResolver;
import com.project.core.web.interceptor.DuplicateSubmissionInterceptor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.unit.DataSize;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {


	private final ProjectConfig projectConfig;

	public WebMvcConfig(ProjectConfig projectConfig) {
		this.projectConfig = projectConfig;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/index").setViewName("index");
	}


	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new FormBeanAttrArgumentResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> excludeLinks = projectConfig.getConfigure().getStaticResources();
		excludeLinks.addAll(projectConfig.getConfigure().getPermitRequest());
		registry.addInterceptor(new DuplicateSubmissionInterceptor()).excludePathPatterns(excludeLinks);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize(DataSize.parse("201400KB")); // KB, MB
		// 设置上传数据总大小
		factory.setMaxRequestSize(DataSize.parse("2014000KB"));

		return factory.createMultipartConfig();
	}
}

