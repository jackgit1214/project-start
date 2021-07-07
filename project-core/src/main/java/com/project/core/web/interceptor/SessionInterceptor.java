package com.project.core.web.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.project.core.web.util.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.servlet.HandlerInterceptor;

@Deprecated
public class SessionInterceptor implements HandlerInterceptor {

    private final Log log = LogFactory.getLog(this.getClass());

    private String FIRSTPAGE = "/";

    /**
     * 在控制类调用之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest _request,
                             HttpServletResponse _response, Object _handler) throws Exception {

        this.log.debug(_request.getRequestURI());
        if (_request.getSession() == null) {
            log.error("session error");
            return turnLogin(_response, _request);
        } else {
            HttpSession session = _request.getSession();
            SessionManager sessionManager = (SessionManager) session
                    .getAttribute("sessionManager");
            if (sessionManager == null) {
                return turnLogin(_response, _request);
            }
        }
        log.debug(_request.getContextPath());
        return true;

    }

    /**
     * 返回登录
     *
     * @param response
     * @return
     * @throws IOException
     */
    private boolean turnLogin(HttpServletResponse response,
                              HttpServletRequest request) throws IOException {
        if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
                .getHeader("X-Requested-With") != null && request.getHeader(
                "X-Requested-With").indexOf("XMLHttpRequest") > -1))) {

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            //PrintWriter out = response.getWriter();

            response.sendRedirect(request.getContextPath() + FIRSTPAGE);
        } else {
            response.setHeader("sessionstatus", "timeout");
            response.setHeader("homepage", request.getContextPath()
                    + this.FIRSTPAGE);
            response.setCharacterEncoding("UTF-8");
        }
        return false;
    }

}
