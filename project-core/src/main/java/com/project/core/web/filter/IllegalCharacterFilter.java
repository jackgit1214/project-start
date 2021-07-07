package com.project.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lilj 使用方法 ，在web.xml文件添加如下段落
 *
 * <filter>
 * <p>
 * <filter-name>IllegalCharacterFilter</filter-name>
 * <p>
 * <filter-class>IllegalCharacterFilter</filter
 * -class>
 *
 * </filter>
 * <p>
 * <filter-mapping>
 * <p>
 * <filter-name>IllegalCharacterFilter</filter-name>
 * <p>
 * <url-pattern>*</url-pattern> 需要过滤的连接，如：*.do,*.action......
 * <p>
 * </filter-mapping>
 */

public class IllegalCharacterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        request = new MHttpServletRequest(request);
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {

    }
}
