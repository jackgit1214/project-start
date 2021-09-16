//package com.project.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Arrays;
//
//@Configuration
//public class CrossConfig {
//
//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        // 允许任何域名使用
//        corsConfiguration.addAllowedOrigin("*");
//        // 允许任何头
//        corsConfiguration.addAllowedHeader("*");
//        // 允许任何方法（POST、GET、OPTIONS等），
//        // 如果需要限制其他的（HEADER、METHOD等）可以进行设置
//        //corsConfiguration.setAllowedMethods(Arrays.asList("GET, POST, DELETE, PUT","OPTIONS"));
//        corsConfiguration.addAllowedMethod("*");
//        return corsConfiguration;
//    }
//
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        // 对接口配置跨域设置
//        source.registerCorsConfiguration("/**", buildConfig());
//        return new CorsFilter(source);
//    }
//
//
//
//}
