package com.project.sso.component;

import com.project.sso.handler.LoginFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class VerifyCodeFilter extends OncePerRequestFilter {

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.info(request.getRequestURI());
//        log.info(request.getMethod());
        if (request.getRequestURI().equals(request.getContextPath() + "/login_processing") && request.getMethod()
                .equalsIgnoreCase("post")) {
            try {
                validate(request);
            } catch (VerifyCodeException e) {
                loginFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        // 3. 校验通过，就放行
        filterChain.doFilter(request, response);
    }

    /* 验证保存在session的验证码和表单提交的验证码是否一致 */
    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        String captcha = ServletRequestUtils.getStringParameter(request, "captcha");
        String code = (String) request.getSession().getAttribute(request.getParameter("uuid"));

        if (!code.equalsIgnoreCase(captcha)) {
            throw new VerifyCodeException("验证码不正确！");
        }
        request.getSession().removeAttribute(request.getParameter("uuid"));
    }
}