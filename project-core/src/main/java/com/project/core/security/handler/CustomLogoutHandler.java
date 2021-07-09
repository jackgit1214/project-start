package com.project.core.security.handler;

import com.project.core.common.anaotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    @SystemLog(moduleId = "退出",description = "用户成功退出",opeType= SystemLog.OpeType.LOGOUT)
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("用户正在退出.......................");
    }

}
