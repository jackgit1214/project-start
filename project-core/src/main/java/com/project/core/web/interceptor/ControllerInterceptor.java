package com.project.core.web.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.core.mybatis.model.QueryModel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 拦截所有Controller
 *
 * @author lilj
 * @date 2021/04/17
 **/
@Slf4j
public class ControllerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            Method method = handlerMethod.getMethod();

            MethodParameter[] parameters = handlerMethod.getMethodParameters();
            Parameter[] ps = method.getParameters();

            for (MethodParameter parameter : parameters) {
                if (parameter.getParameterType() == QueryModel.class) {

                    log.info(parameter.toString());
                }
            }

            Map ParameterMap = request.getParameterMap();

            Set<Map.Entry<String, String[]>> entry = ParameterMap.entrySet();
            Iterator<Map.Entry<String, String[]>> it = entry.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String[]> me = it.next();
                String key = me.getKey();
                String value = me.getValue()[0];
                log.info(key);
            }

        }
        log.info(handler.getClass().getName() + "...........postHandle............................");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info(handler.getClass().getName() + "...........afterCompletion............................");
    }

}
