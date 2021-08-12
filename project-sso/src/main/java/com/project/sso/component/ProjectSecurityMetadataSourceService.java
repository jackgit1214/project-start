package com.project.sso.component;


import com.project.core.mybatis.model.FunModule;
import com.project.core.mybatis.model.SysRole;
import com.project.core.web.config.ProjectConfig;

import com.project.sso.service.IModuleSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@Slf4j
public class ProjectSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    private final ProjectConfig projectConfig;
    private final IModuleSecurityService sysModuleService;
    //路径请求以及资源列表
    private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();


    public ProjectSecurityMetadataSourceService(ProjectConfig projectConfig, IModuleSecurityService sysModuleService) {
        this.projectConfig = projectConfig;
        this.sysModuleService = sysModuleService;
    }


    /**
     * 初始化系统所有资源
     */
    @PostConstruct
    public void init() {
        List<FunModule> modules = sysModuleService.getSysModules();
        modules.forEach(item -> {
            Set<SysRole> roles = item.getRoles();
            if (roles != null) {
                List<ConfigAttribute> configAttributes = new ArrayList<>();
                for (SysRole role : roles) {
                    if (role.getRoleId() != null && !"".equals(role.getRoleId()))
                        configAttributes.add(new SecurityConfig(role.getRoleId()));
                }
                if (item.getUrlLink() != null && !"".equals(item.getUrlLink().trim()) && configAttributes.size() != 0) {
                    requestMap.put(new AntPathRequestMatcher(item.getUrlLink()), configAttributes);
                }
            }
        });
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        if (requestMap.isEmpty()) {
            init();
        }
        final HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                // log.info("[自定义权限资源数据源]：当前路径[{}]需要的资源权限:[{}] ==> 触发鉴权决策管理器", entry.getKey(), entry.getValue().toString());
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        log.info("[自定义权限资源数据源]：获取所有的角色==> {}", allAttributes);
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
