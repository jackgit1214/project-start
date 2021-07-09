package com.project.core.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.power.common.util.IpUtil;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.model.SysLog;
import com.project.core.mybatis.service.LogDetailService;
import com.project.core.security.model.UserInfo;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class SystemLogAop {

    private final LogDetailService logDetailService;

    private final ObjectMapper objectMapper;

    public SystemLogAop(LogDetailService logDetailService, ObjectMapper objectMapper) {
        this.logDetailService = logDetailService;
        this.objectMapper = objectMapper;
    }

    @Pointcut("@annotation(com.project.core.common.anaotation.SystemLog)")
    private void logHandle() {

    }

    @Pointcut("execution(* org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler.logout(..))")
    private void logout() {

    }

    @Around(value = "logHandle() || logout() ")
    public Object actionAopAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //日志的拦截应包含的信息
        //1、数据信息：及本次拦截取数据信息，包含请求参数、方法参数数据等
        //2、请求信息：请求类名、请求方法名、请求花费时间（需要计算）、请求时间、请求人、请求IP，访问地址
        //3、用户填写信息：操作类型（修改、删除、数据查询、）、模块名称或ID、操作描述

        Assert.notNull(logDetailService, "日志服务 不能为空");
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        Assert.notNull(sra, "ServletRequestAttributes 不能为空");
        HttpServletRequest request = sra.getRequest();
        Object rs;

        Object[] params = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        long startTime = System.currentTimeMillis();

        //创建日志对象类
        SysLog sysLog = new SysLog();
        sysLog.setLogId(UUIDUtil.getUUID());
        //用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null){
            UserInfo userInfo = (UserInfo) authentication.getPrincipal();
            sysLog.setUserId(userInfo.getPrimaryKey().toString());
            sysLog.setUserName(userInfo.getUsername());
        }

        sysLog.setLogTime(new Date(startTime));
        sysLog.setRequestIp(IpUtil.getIpAddr(request));
        sysLog.setRequestClassMethod(joinPoint.getTarget().getClass()+"."+methodSignature.getMethod().getName());
        sysLog.setRequestUrl(request.getRequestURL().toString());
        sysLog.setLogType(1);

        //注解下的信息
        SystemLog annotation = methodSignature.getMethod().getAnnotation(SystemLog.class);
        sysLog.setModelName(annotation.moduleId());
        sysLog.setOpeType(String.valueOf(annotation.opeType().ordinal()));
        sysLog.setDescription(annotation.description());

        Map<String, Object> paramsJson = new HashMap<>();
        try {
            String queryString = request.getQueryString();
            paramsJson.put("queryString",queryString);
            String[] parameterNames = methodSignature.getParameterNames();

            for (int i = 0; i < parameterNames.length; i++) {

                //request与response不处理
                if (params[i] instanceof HttpServletRequest) continue;
                if (params[i] instanceof HttpServletResponse) continue;
                if (params[i] instanceof Authentication) continue;
                paramsJson.put(parameterNames[i], params[i]);
            }
            sysLog.setReqParam(objectMapper.writeValueAsString(paramsJson));

            rs = joinPoint.proceed();
        } finally {
            long exeT = System.currentTimeMillis() - startTime;
            sysLog.setExecuteTime((int)exeT);
            this.logDetailService.saveLogInfo(sysLog);
        }
        return rs;
    }

}
