package com.project.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final String SIGNING_KEY="ATTK-KEY";

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients(); //允许客户端访问 OAuth2 授权接口，否则请求 token 会返回 401。
        security.tokenKeyAccess("isAuthenticated()"); //允许已授权用户访问获取 token 接口
        security.checkTokenAccess("isAuthenticated()");//允许已授权用户访问获取 checkToken 接口
       // security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
       // security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

//        new ClientDetailsServiceBuilder().build();
        ClientDetailsService cds = new JdbcClientDetailsServiceBuilder().dataSource(dataSource).build();
        clients.jdbc(dataSource);

    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.accessTokenConverter(jwtAccessTokenConverter());
        endpoints.tokenStore(jwtTokenStore());
//        endpoints.tokenServices(tokenServices());
//        endpoints.tokenServices(defaultTokenServices());
    }

//    @Bean
//    AuthorizationServerTokenServices tokenServices() {
//        DefaultTokenServices services = new DefaultTokenServices();
//        services.setClientDetailsService(clientDetailsService);
//        services.setSupportRefreshToken(true);
//        services.setTokenStore(jwtTokenStore());
//        services.setAccessTokenValiditySeconds(60);
//        //services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
//        return services;
//    }

//    @Primary
//    @Bean
//    public DefaultTokenServices defaultTokenServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(jwtTokenStore());
//        defaultTokenServices.setSupportRefreshToken(true);
//        return defaultTokenServices;
//    }



    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);   //  Sets the JWT signing key
        return jwtAccessTokenConverter;
    }

}
