package com.project.core.security.config;

import com.project.core.common.util.MD5Util;
import com.project.core.security.ProjectUserDetailsService;
import com.project.core.security.component.CustomSessionExpiredStrategy;
import com.project.core.security.component.DisableUrlSessionFilter;
import com.project.core.security.component.UnauthorizedEntryPoint;
import com.project.core.security.component.VerifyCodeFilter;
import com.project.core.security.handler.CustomLogoutHandler;
import com.project.core.security.handler.CustomLogoutSuccessHandler;
import com.project.core.security.handler.LoginFailureHandler;
import com.project.core.security.handler.LoginSuccessHandler;
import com.project.core.web.config.ProjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final LoginSuccessHandler successHandler;
    private final LoginFailureHandler failureHandler;
    private final VerifyCodeFilter verifyCodeFilter;
    private final ProjectUserDetailsService userDetailsService;
    private final UnauthorizedEntryPoint unauthorizedEntryPoint;
    private final ProjectConfig projectConfig;
    private final DisableUrlSessionFilter disableUrlSessionFilter;

    public WebSecurityConfig(LoginSuccessHandler successHandler, LoginFailureHandler failureHandler, VerifyCodeFilter verifyCodeFilter
            , ProjectUserDetailsService userDetailsService, UnauthorizedEntryPoint unauthorizedEntryPoint
            , ProjectConfig projectConfig, DisableUrlSessionFilter disableUrlSessionFilter) {
        this.verifyCodeFilter = verifyCodeFilter;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.userDetailsService = userDetailsService;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
        this.projectConfig = projectConfig;
        this.disableUrlSessionFilter = disableUrlSessionFilter;
     }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }


    //
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (projectConfig.getConfigure().isMd5Encoder())
            //使用md5加密的方法,主要为兼容旧的加密方法
            auth.userDetailsService(userDetailsService).passwordEncoder(new Md5PasswordEncoder());
        else
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        List<String> staticResources = projectConfig.getConfigure().getStaticResources();
        staticResources.forEach(resources -> {
            web.ignoring().antMatchers(resources);
        });
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitRequest = projectConfig.getConfigure().getPermitRequest();
        String[] param = permitRequest.toArray(new String[permitRequest.size()]);
        http.authorizeRequests()
                .antMatchers(param).permitAll()
                .anyRequest().authenticated();

        //登录处理
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProcessing")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(customLogoutHandler)
//                .logoutSuccessHandler(customLogoutSuccessHandler)
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true);

        http.sessionManagement()
                .invalidSessionUrl("/login");
        /* 防止再次登录 此时不会踢出之前的登录者，而是先登录者建立了唯一session，在他注销或关闭浏览器之前，不允许异地再次登录。*/
        /* 防止再次登录 此时会踢出之前的登录者，再去发出请求会跳转到过期url。*/
        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false) //为true其它客户端不允许登录，false允许登录，其它客户端会退出
                .expiredUrl("/login")
                .expiredSessionStrategy(new CustomSessionExpiredStrategy());

        //记住我功能，cookies有限期是一周
        //登陆时是否激活记住我功能的参数名字，在登陆页面有展示
        //cookies的名字，登陆后可以通过浏览器查看cookies名字
        http.rememberMe()
                .tokenValiditySeconds(projectConfig.getConfigure().getRememberTokenTime())
                .rememberMeParameter("remember-me") //前端传递参数名
                .rememberMeCookieName(projectConfig.getConfigure().getProjectEngName());

        /** 这里可以自定义 */
        http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint); /* 用来解决匿名用户访问无权限资源时的异常 */
        http.headers().frameOptions().sameOrigin();
        //验证码的校验
        if (projectConfig.getConfigure().isLoginCaptcha())
            http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(disableUrlSessionFilter, UsernamePasswordAuthenticationFilter.class);
        // CSRF是指跨站请求伪造,
        //从Spring Security 4.0开始，默认情况下会启用CSRF保护，开通后任何调用需要在请求头中添加csrf参数
        // 去除csrf处理，去除后，需增加csrf过滤器的处理

        if (!projectConfig.getConfigure().isCsrf())
            http.csrf().disable().cors();
    }


    class Md5PasswordEncoder implements PasswordEncoder {

        @Override
        public String encode(CharSequence charSequence) {
            return MD5Util.encode((String) charSequence);
        }

        @Override
        public boolean matches(CharSequence charSequence, String encodedPassword) {
            return encodedPassword.equals(MD5Util.encode((String) charSequence));
        }
    }
}
