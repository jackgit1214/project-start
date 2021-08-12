package com.project.sso.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();


    @Override
    @SystemLog(moduleId = "用户登录",description = "用户登录失败",opeType= SystemLog.OpeType.LOGIN)
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // log.info(exception.toString());
        log.error("登录错误 [{}] ", exception.getMessage());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(MAPPER.writeValueAsString(new BaseResult(ReturnCode.FAILED.getCode(), exception.getMessage())));
        writer.flush();
        writer.close();
    }
}
