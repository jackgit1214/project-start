package com.project.sso.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

/**
 * Security拦截器
 *
 * @author lilj
 * @date 2021/03/16
 **/
@Component
@Slf4j
public class ProjectSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private static final String FILTER_APPLIED = "__spring_security_CustomFilterSecurityInterceptor_filterApplied";
    private final ProjectAccessDecisionManager projectAccessDecisionManager;
    private final ProjectSecurityMetadataSourceService projectSecurityMetadataSourceService;

    public ProjectSecurityInterceptor(ProjectAccessDecisionManager projectAccessDecisionManager, ProjectSecurityMetadataSourceService projectSecurityMetadataSourceService) {
        this.projectAccessDecisionManager = projectAccessDecisionManager;
        this.projectSecurityMetadataSourceService = projectSecurityMetadataSourceService;
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.projectSecurityMetadataSourceService;
    }


    @PostConstruct
    public void init() {
        super.setAccessDecisionManager(this.projectAccessDecisionManager);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    private void invoke(FilterInvocation fi) throws IOException, ServletException {
        if ((fi.getRequest() != null) && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)) {
            // filter already applied to this request and user wants us to observe
            // once-per-request handling, so don't re-do security checking
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            // first time this request being called, so perform security checking
            if (fi.getRequest() != null) {
                fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            }

            /* 调用父类的beforeInvocation ==> accessDecisionManager.decide(..) */
            InterceptorStatusToken token = super.beforeInvocation(fi);

            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            } finally {
                super.finallyInvocation(token);
            }
            super.afterInvocation(token, null);
        }
    }
}
