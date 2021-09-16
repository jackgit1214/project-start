//package com.project.resserver.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
///**
// * OAuth2 结合 JWT 验证
// */
//@Configuration
//public class AccessTokenConfig {
//
//    private final String SIGNING_KEY="ATTK-KEY";
//
//    @Bean
//    public JwtTokenStore jwtTokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);   //  Sets the JWT signing key
//        return jwtAccessTokenConverter;
//    }
//
//}
