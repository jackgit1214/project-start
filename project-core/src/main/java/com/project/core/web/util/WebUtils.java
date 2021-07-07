package com.project.core.web.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lilj
 */
public class WebUtils {

    /**
     * 用JavaScript显示一个弹出信息，并且后退回原来页面
     *
     * @param info
     * @param response
     * @throws IOException
     */
    public static void alertAndBack(String info, HttpServletResponse response) throws IOException {
        if (response == null)
            return;
        response.setContentType("text/html");

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("    alert('" + info + "');");
        out.println("    history.go(-1);");
        out.println("</script>");
    }

    public static void alertAndGo(String info, String url, HttpServletResponse response) throws IOException {
        if (response == null)
            return;
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("    alert('" + info + "');");
        out.println("    document.location = \"" + url + "\";");
        out.println("</script>");
    }

    public static void confirmAndGo(String info, String url1, String url2, HttpServletResponse response) throws IOException {
        if (response == null)
            return;
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("  if(confirm('" + info + "')){");
        out.println("    document.location = \"" + url1 + "\";");
        out.println("  }else{");
        out.println("    document.location = \"" + url2 + "\";");
        out.println("  }");
        out.println("</script>");
    }

    public static void alertAndGoClose(String info, HttpServletResponse response) throws IOException {
        if (response == null)
            return;
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("    alert('" + info + "');");
        out.println("    window.close();");
        out.println("</script>");
    }

    public static void alertAndOpen(String info, String url, HttpServletResponse response) throws IOException {
        if (response == null)
            return;
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("    alert('" + info + "');");
        out.println("    window.open ( \"" + url + "\");");
        out.println("</script>");
    }

    public static void alert(String info, HttpServletResponse response) throws IOException {
        if (response == null)
            return;
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("    alert('" + info + "');");
        out.println("</script>");
    }


    /**
     * 显示错误日志信息
     */
    public static void showErrorMessage(HttpServletResponse response) throws IOException {
        if (response == null)
            return;
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("    ");
        out.println("    history.go(-1);");
        out.println("    exceptionMess.showDialog();");
        out.println("</script>");
    }
}
