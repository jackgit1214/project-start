package com.project.sso.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 自定义权限控制器
 *
 * @author lilj
 * @date 2021/27/18 15:27
 * @return
 */
@Service
@Slf4j
public class ProjectAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {

        if (configAttributes == null) {
            return;
        }

        for (ConfigAttribute configAttribute : configAttributes) {

            /* 资源需要的权限 */
            String attribute = configAttribute.getAttribute();

            /* 用户的权限 */
            for (GrantedAuthority authority : authentication.getAuthorities()) { // 当前用户的权限
                //比较，相同时则有权限
                if (attribute.trim().equals(authority.getAuthority().trim())) {
                    // log.info("[鉴权决策管理器]：登录用户[{}]权限匹配",authentication.getName());
                    return;
                }
            }
        }
        log.info("[鉴权决策管理器]：登录用户[{}]权限不足", authentication.getName());
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
