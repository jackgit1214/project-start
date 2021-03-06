package com.project.core.common.aspect;

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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@ConditionalOnBean(LogDetailService.class)
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

        //?????????????????????????????????
        //1?????????????????????????????????????????????????????????????????????????????????????????????
        //2???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????IP???????????????
        //3?????????????????????????????????????????????????????????????????????????????????????????????ID???????????????

        Assert.notNull(logDetailService, "???????????? ????????????");
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        Assert.notNull(sra, "ServletRequestAttributes ????????????");
        HttpServletRequest request = sra.getRequest();
        Object rs;

        Object[] params = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        long startTime = System.currentTimeMillis();

        //?????????????????????
        SysLog sysLog = new SysLog();
        sysLog.setLogId(UUIDUtil.getUUID());
        //????????????
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

        //??????????????????
        SystemLog annotation = methodSignature.getMethod().getAnnotation(SystemLog.class);
        String moduleName = annotation.moduleId();
        if (!StringUtils.hasText(moduleName)){
            SystemLog annClass =  joinPoint.getTarget().getClass().getAnnotation(SystemLog.class);
            if (annClass == null)
                moduleName = "???????????????";
            else
                moduleName = annClass.moduleId();
        }
        sysLog.setModelName(moduleName);
        sysLog.setDescription(annotation.description());
        sysLog.setOpeType(String.valueOf(annotation.opeType().ordinal()));
        Map<String, Object> paramsJson = new HashMap<>();
        try {
            String queryString = request.getQueryString();
            paramsJson.put("queryString",queryString);
            String[] parameterNames = methodSignature.getParameterNames();

            for (int i = 0; i < parameterNames.length; i++) {

                //request ???response???Authentication?????????
                if (params[i] instanceof HttpServletRequest) continue;
                if (params[i] instanceof HttpServletResponse) continue;
                if (params[i] instanceof Authentication) continue;
                if (params[i] instanceof MultipartFile){
                    MultipartFile file = (MultipartFile)params[i];
                    paramsJson.put(parameterNames[i], file.getOriginalFilename());
                    continue;
                }
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
