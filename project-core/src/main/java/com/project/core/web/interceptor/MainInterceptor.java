/**
 *
 */
package com.project.core.web.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.core.web.config.ResourcePathConfig;
import com.project.core.web.util.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.project.core.common.anaotation.LinkHistoryAnaotation;

@Slf4j
@Deprecated
public class MainInterceptor implements HandlerInterceptor {

    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>(
            "StopWatch-StartTime");

    private final ResourcePathConfig resourcePathConfig;

    public MainInterceptor(ResourcePathConfig resourcePathConfig) {
        this.resourcePathConfig = resourcePathConfig;
    }

    /**
     * 在控制类调用之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest _request,
                             HttpServletResponse _response, Object _handler) throws Exception {

        //String requestUri = _request.getRequestURI();
        _response.addHeader("Access-Control-Allow-Origin", "*");
        _response.addHeader("Access-Control-Allow-Methods", "*");
//		_response.addHeader("Access-Control-Max-Age","100");
//		_response.addHeader("Access-Control-Allow-Headers", "Content-Type");
//		_response.addHeader("Access-Control-Allow-Credentials","false");
        //log.debug(_request.getContextPath());
        //boolean rtn = super.preHandle(_request, _response, _handler);
        return true;

    }


    /**
     * 执行控制类方法之后,视图渲染之前 postHandle(..) is called after the handler is executed;
     */
    @Override
    public void postHandle(HttpServletRequest _request,
                           HttpServletResponse _response, Object _handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
        // _modelAndView.setViewName("/responseMessage");

        HttpSession session = _request.getSession();

        SessionManager sessionManager = (SessionManager) session
                .getAttribute("sessionManager");
        if (sessionManager != null) {
            HandlerMethod handlerMethod = (HandlerMethod) _handler;
            Method method = handlerMethod.getMethod();

            LinkHistoryAnaotation annotation = method
                    .getAnnotation(LinkHistoryAnaotation.class);
            if (annotation != null) {
                String level = annotation.linkLevel();
                String key = annotation.linkName();
                String value = annotation.linkValue();
                sessionManager.setHistory(level, key, value);
            }
        }

        // 存放资源路径
        if (modelAndView != null) {
            // 上传图片路径及地址
            // 资源路径

            modelAndView.addObject("defaultResourceUrl", resourcePathConfig.getResourceUrl());
            // 根据数据ID取得的资源路径
            modelAndView.addObject("dataResourceUrl", resourcePathConfig.getDataResourceUrl());
            // image资源路径
            modelAndView.addObject("imageResourceUrl", resourcePathConfig.getImageResourceUrl());
            // 类型资源路径
            modelAndView.addObject("typeResourceUrl", resourcePathConfig.getTypeResourceUrl());

            // 其它资源路径
            modelAndView.addObject("otherResourceUrl", resourcePathConfig.getOtherResourceUrl());
            modelAndView.addObject("dataDefaultImageResourceUrl",
                    resourcePathConfig.getDataDefaultImageResourceUrl());
        }

        //super.postHandle(_request, _response, _handler, modelAndView);

    }

    /**
     * afterCompletion(..) is called after the complete request has finished.
     * 视图渲染之后
     */
    @Override
    public void afterCompletion(HttpServletRequest _request,
                                HttpServletResponse _response, Object _handler, Exception _ex)
            throws Exception {
        // long endTime = System.currentTimeMillis();//2、结束时间
        // long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
        // long consumeTime = endTime - beginTime;//3、消耗的时间
        // //System.out.println(consumeTime);
        // if(consumeTime > 500) {//此处认为处理时间超过500毫秒的请求为慢请求
        // System.out.println(String.format("%s consume %d millis",
        // _request.getRequestURI(), consumeTime));
        // }
        //super.afterCompletion(_request, _response, _handler, _ex);
    }


    private void typeget(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        Type[] types = method.getGenericParameterTypes();
        if (types.length > 0) {
            for (Type type : types) {

                if (type instanceof BindingResult) {
                    log.info("klsdjfa);sdf");
                }
            }
        }

    }

    private Object processParam(HandlerMethod handlerMethod) {

        Method method = handlerMethod.getMethod();
        Class<?>[] classes = method.getParameterTypes();
        // ReflectionUtils.

        if (classes.length > 0) {
            for (Class<?> classz : classes) {
                if (classz.equals(BindingResult.class)) {
                    // MethodParameter methodParam = new MethodParameter(method,
                    // 1);
                    MethodParameter methodParam = handlerMethod
                            .getMethodParameters()[1];
                    methodParam.getParameterName();
                    Exception thrownException = null;
                    GenericTypeResolver.resolveParameterType(methodParam,
                            handlerMethod.getClass());
                    Class<?> paramType = methodParam.getParameterType();
                    // ParameterizedType pt = null;
                    // pt.getActualTypeArguments();
                    Type[] types = method.getGenericParameterTypes();
                    for (Type type : types) {
                        if (type instanceof ParameterizedType)
                            log.info("0000000000000000");
                    }

                }
            }
        }
        return null;
    }

}
