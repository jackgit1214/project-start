package com.project.config;

import com.project.core.common.util.ApplicationContextUtil;
import com.project.sso.service.IModuleSecurityService;
import com.project.sso.service.IUserSecurityService;
import com.project.system.service.impl.SysModuleServiceImpl;
import com.project.system.service.impl.SysUserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfigurer {

    @Bean
    public IUserSecurityService userService() {
        return ApplicationContextUtil.getBean(SysUserServiceImpl.class);
    }

    @Bean
    public IModuleSecurityService sysModuleService() {
        return ApplicationContextUtil.getBean(SysModuleServiceImpl.class);
    }

}
