package com.project.core.security.handler;

import com.project.core.common.anaotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    @SystemLog(moduleId = "首页用户退出", description = "用户退出成功", opeType = SystemLog.OpeType.LOGOUT)
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("用户退出！");
        //super.onLogoutSuccess(request, response, authentication);
    }

}
