package com.project.sso.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.power.common.util.IpUtil;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.model.SysLog;
import com.project.core.mybatis.model.UserInfo;
import com.project.core.mybatis.service.LogDetailService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@ConditionalOnBean(LogDetailService.class)
public class AuthorizationLogAop {

    private final LogDetailService logDetailService;

    private final String[] RequestMaps = {"/oauth/token","/oauth/token"};
    private final ObjectMapper objectMapper;

    public AuthorizationLogAop(LogDetailService logDetailService, ObjectMapper objectMapper) {
        this.logDetailService = logDetailService;
        this.objectMapper = objectMapper;
    }

    @Pointcut("@within(org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void logHandle() {

    }

    @Around(value = "logHandle() ")
    public Object actionAopAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //日志的拦截应包含的信息
        //1、数据信息：及本次拦截取数据信息，包含请求参数、方法参数数据等
        //2、请求信息：请求类名、请求方法名、请求花费时间（需要计算）、请求时间、请求人、请求IP，访问地址
        //3、用户填写信息：操作类型（修改、删除、数据查询、）、模块名称或ID、操作描述
        Assert.notNull(logDetailService, "日志服务 不能为空");

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        Assert.notNull(requestAttributes, "ServletRequestAttributes 不能为空");
        HttpServletRequest request = requestAttributes.getRequest();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Object rs ;
        long startTime = System.currentTimeMillis();
        SysLog sysLog = new SysLog() ;
        try {
            sysLog = this.generateLogData(joinPoint,request,methodSignature);
            rs = joinPoint.proceed();
        } finally {
            long exeT = System.currentTimeMillis() - startTime;
            sysLog.setExecuteTime((int)exeT);
            this.logDetailService.saveLogInfo(sysLog);
        }
        return rs;
    }

    private Map<String, Object> getLoginInfo(Object[] params,RequestMapping annotation){
        String mapPath = annotation.value()[0];
        String mapMethod = annotation.method()[0].toString();
        Map<String, Object> userInfo = new HashMap<>();
        if (mapPath.equals("/oauth/token")){
             userInfo  = (Map<String, Object>)params[1]; //取第二个参数

        }else if (mapPath.equals("/oauth/authorize")){
            userInfo  = (Map<String, Object>)params[0]; //取第二个参数
        }
        return userInfo;
    }

    private SysLog generateLogData(ProceedingJoinPoint joinPoint,HttpServletRequest request,MethodSignature methodSignature ) throws JsonProcessingException {

        RequestMapping annotation = methodSignature.getMethod().getAnnotation(RequestMapping.class);
        SysLog sysLog = new SysLog();
        sysLog.setLogId(UUIDUtil.getUUID());
        long startTime = System.currentTimeMillis();
        sysLog.setLogTime(new Date(startTime));
        sysLog.setRequestIp(IpUtil.getIpAddr(request));
        sysLog.setRequestClassMethod(joinPoint.getTarget().getClass()+"."+methodSignature.getMethod().getName());
        sysLog.setRequestUrl(request.getRequestURL().toString());
        sysLog.setLogType(2);
        sysLog.setOpeType("4");

        //sso信息数据
        Map<String,Object> userInfo = this.getLoginInfo(joinPoint.getArgs(),annotation);
        if (userInfo!=null){
            String clientId = userInfo.get("client_id").toString();
            sysLog.setUserId(clientId);
            sysLog.setUserName(userInfo.get("username").toString());
            sysLog.setDescription(clientId+"登录系统");
        }
        sysLog.setModelName("sso"); //单点登录日志
        sysLog.setReqParam(objectMapper.writeValueAsString(userInfo));
        return sysLog;
    }
}
