//package com.project.sso.config;
//
//import org.assertj.core.util.Lists;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * 部分接口需要跨域支持
// */
//@Configuration
//public class CustomCorsConfiguration {
//
//    @Bean
//    public CorsFilter customCorsFilter() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        CorsFilter corsFilter = new CorsFilter(urlBasedCorsConfigurationSource);
//
//        return corsFilter;
//    }
//
////    @Bean
////    public FilterRegistrationBean corsFilter() {
////        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
////        registrationBean.setFilter(customCorsFilter());
////        registrationBean.setOrder(1);
////        return registrationBean;
////    }
//}
