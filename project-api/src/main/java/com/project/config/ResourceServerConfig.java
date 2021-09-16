package com.project.config;

import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysUser;
import com.project.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    final String JWT_SIGN_KEY = "ATTK-KEY";

    @Autowired
    private SysUserService sysUserService;
    /**
     * api帮助文档需要处理的
     */
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/v3/api-docs",
            "/webjars/**",
            "/apihelp",
            "/doc.html",
            "/swagger-ui/**"
    };


//    @Bean
//    public ResourceServerTokenServices tokenServices() {
//        //向远程认证服务器获取token，同时获取token的用户信息
//        RemoteTokenServices remoteService = new RemoteTokenServices();
//        remoteService.setCheckTokenEndpointUrl("http://localhost:9001/oauth/check_token");
//        remoteService.setClientId("UserManagement");
//        remoteService.setClientSecret("user123");
//        DefaultAccessTokenConverter defaultAccessTokenConverter=new DefaultAccessTokenConverter();
//        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
//        userAuthenticationConverter.setUserDetailsService(new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                QueryModel queryModel = new QueryModel();
//                queryModel.createCriteria().andEqualTo("logincode",username);
//                SysUser user = sysUserService.findObjects(queryModel).get(0);
//                return user;
//            }
//        });
//        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
//        remoteService.setAccessTokenConverter(defaultAccessTokenConverter);
//        return remoteService;
//    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //resources.resourceId("res1").tokenServices(tokenServices());//.tokenStore(tokenStore());
          resources.resourceId("res1").tokenStore(tokenStore());
//        resources.stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
//                .antMatchers("/test/**").authenticated() // 匹配以/test开头的请求，需要授权
                .anyRequest().authenticated();

        http.cors().and().csrf().disable(); //开启跨域，以及禁用csrf
    }

    @Bean
    TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(JWT_SIGN_KEY);   //  Sets the JWT signing key
        DefaultAccessTokenConverter defaultAccessTokenConverter=new DefaultAccessTokenConverter();
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                QueryModel queryModel = new QueryModel();
//                queryModel.createCriteria().andEqualTo("logincode",username);
                SysUser user = (SysUser) sysUserService.findUserByLoginName(username);
                return user;
            }
        });
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter);
        return jwtAccessTokenConverter;
    }

}