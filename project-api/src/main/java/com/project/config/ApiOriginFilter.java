//package com.project.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.filter.OncePerRequestFilter;
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
////@WebFilter(urlPatterns = "*")
//@Slf4j
//@Component
//public class ApiOriginFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = request.getHeader("Authorization");
//        log.info(token);
//        if ( request.getMethod().equals(RequestMethod.OPTIONS.name()) ) {
//            filterChain.doFilter(request,response);
//            return;
//        }
//        token = request.getHeader("Authorization");
//        log.info(token);
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//
//
//}
