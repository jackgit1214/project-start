package com.project.core.web.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.core.mybatis.model.UserInfo;
import com.project.core.web.util.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.util.RandomGUID;

/**
 * @author lilj 防止重复提交过滤器
 */
@Slf4j
public class DuplicateSubmissionInterceptor implements HandlerInterceptor {

    private String curToken;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken )
            return true;

        if ( handler instanceof HandlerMethod) {

//            UserInfo userInfo = (UserInfo) authentication.getPrincipal();
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            DuplicateSubmission annotation = method
                    .getAnnotation(DuplicateSubmission.class);
            if (annotation != null) {
                log.debug(request.getRequestURI().toString());
                boolean needSaveSession = annotation.needSaveToken();
                if (needSaveSession) {
                    RandomGUID token = new RandomGUID();
                    request.getSession(false).setAttribute(annotation.tokenName(),
                            token.toString());
                }

                boolean needRemoveSession = annotation.needRemoveToken();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request, annotation.tokenName())) {
                        log.warn("请不要重复提交!!!");
//                        log.warn("请不要重复提交,[用户:"
//                                + userInfo.getUsername() + ",url:"
//                                + request.getServletPath() + "]");
                        return false;
                    }
                    // request.getSession(false).removeAttribute("token");
                }
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        DuplicateSubmission annotation = null;
        String tokenName = "token";
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            annotation = method.getAnnotation(DuplicateSubmission.class);
            if (annotation != null)
                tokenName = annotation.tokenName();
        }

        if (ex != null) {
            request.getSession(false).setAttribute(tokenName, this.curToken);
        }
        //super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        DuplicateSubmission annotation = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            annotation = method.getAnnotation(DuplicateSubmission.class);
        }

        if (annotation != null) {
            boolean needRemoveSession = annotation.needRemoveToken();
            if (needRemoveSession) {
                Object obj = request.getAttribute("isSuccess");
                if (obj != null) {
                    boolean isSuccess = Boolean.valueOf(request.getAttribute("isSuccess").toString());
                    if (isSuccess)
                        request.getSession(false).removeAttribute(annotation.tokenName());
                }
            }
        }
        //super.postHandle(request, response, handler, modelAndView);
    }

    private boolean isRepeatSubmit(HttpServletRequest request, String tokenName) {
        String serverToken = (String) request.getSession(false).getAttribute(tokenName);
        if (serverToken == null) {
            return true;
        }
        this.curToken = serverToken;
        String clientToken = request.getParameter(tokenName);
        if (clientToken == null) { // 如果前段没有token值，则可以提交
            return false;
        }
        return !serverToken.equals(clientToken);
    }

}
