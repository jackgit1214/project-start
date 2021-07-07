package com.project.core.security;

import com.project.core.mybatis.service.IUserSecurityService;
import com.project.core.security.model.UserInfo;
import com.project.core.web.config.ProjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 根据配置从spring容器中加载userService,
 * userService实现接口，根据登录用户登录名取得用户信息
 * 返回的user对象，须实现UserDetails接口
 *
 * @author lilj
 * @date 2021/3/31 10:00
 */
@Component
@Slf4j
public class ProjectUserDetailsService implements UserDetailsService {

    private final ProjectConfig projectConfig;
    private final IUserSecurityService userService;

    public ProjectUserDetailsService(ProjectConfig projectConfig, IUserSecurityService userService) {
        this.projectConfig = projectConfig;
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {

        UserInfo user = userService.findUserByLoginName(loginName);
        if (user == null) {
            log.error("用户 [{}] 未找到", loginName);
            throw new UsernameNotFoundException("Username：[" + loginName + "] not found");
        }
        return user;
    }


    public IUserSecurityService getUserService() {
        return userService;
    }
}
