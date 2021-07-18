/**
 *
 */
package com.project.core.web.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.core.common.converter.*;
import com.project.core.common.exception.BusinessException;
import com.project.core.common.response.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    public Object defaultErrorHandler(HttpServletRequest req, BusinessException e, HttpServletResponse response) {

        BaseResult result = new BaseResult(e.getErrorCode(), e.getErrorMsg());
        result.setData(e.getMessage());
        ModelAndView mav = new ModelAndView();
        if (!(req.getHeader("accept").indexOf("application/json") > -1 || (req
                .getHeader("X-Requested-With") != null && req.getHeader(
                "X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
            //转发到/error
            mav.setViewName("error/error");
            mav.addObject("map", result);
            return mav;
        } else {
            try {
                PrintWriter writer = response.getWriter();
                writer.write(objectMapper.writeValueAsString(result));
                writer.flush();
                return null;
            } catch (Exception exception) {

            }
            return null;
        }
    }

    @ExceptionHandler(BusinessException.class)
    public String handlerException(BusinessException e, HttpServletRequest request) {
        BaseResult result = new BaseResult(e.getErrorCode(), e.getErrorMsg());
        log.info(e.getMessage());
        result.setData(e.getMessage());
        request.setAttribute("javax.servlet.error.status_code", result.getCode());
        request.setAttribute("res", result);
        return "forward:/error";
    }

    @ExceptionHandler(value = Exception.class)
    public String defaultErrorHandler(HttpServletRequest req, Exception e, HttpServletResponse response) throws Exception {

        log.info(e.getMessage());
        BaseResult result = new BaseResult(9999, e.toString());
        result.setData(objectMapper
                .writeValueAsString(e));
        //设置错误状态码，一定要设置，否者就不会进入到自定义页面中
        req.setAttribute("javax.servlet.error.status_code", result.getCode());
        //将自己的异常信息加入到request
        req.setAttribute("res", result);
        return "forward:/error";
    }


    /**
     * ajax调用的绑定异常
     *
     * @param ex
     * @param request
     * @param nw
     * @param rq
     * @param response
     * @return
     */
    @ExceptionHandler(BindException.class)
//	@ResponseBody
    public String exceptionHandler(BindException ex, WebRequest request,
                                   NativeWebRequest nw, HttpServletRequest rq,
                                   HttpServletResponse response) {

        Map<String, String> hm = new HashMap<String, String>();
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
        String message = "";
        for (ObjectError error : errors) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                message = message + "[" + fieldError.getField() + "]["
                        + fieldError.getRejectedValue() + "]";
            }
            message = message + error.getDefaultMessage() + "\r\n";
        }
        hm.put("error", message);
        BaseResult result = new BaseResult(9999, message);
        result.setData(hm);
        rq.setAttribute("javax.servlet.error.status_code", result.getCode());
        rq.setAttribute("res", result);
        return "forward:/error";
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {

        // 注册int,float,long,double类型转换

        binder.registerCustomEditor(int.class, new CustomIntegerEditor());
        binder.registerCustomEditor(float.class, new CustomFloatEditor());
        binder.registerCustomEditor(double.class, new CustomDoubleEditor());
        binder.registerCustomEditor(long.class, new CustomLongEditor());
        binder.registerCustomEditor(short.class, new CustomShortEditor());
        // 注册boolean类型转换，缺省值为false
        binder.registerCustomEditor(boolean.class, new SpecCustomBooleanEditor(
                false));

        // 注册日期转换
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, true));

    }


    class AjaxResInfo {
        private String fieldname;
        private Map<String, String> errorInfo;

        public void putInfo(String fieldName, String msg) {
            if (this.errorInfo.containsKey(fieldName)) {

            }
        }

        public String getFieldname() {
            return fieldname;
        }

        public void setFieldname(String fieldname) {
            this.fieldname = fieldname;
        }

        public Map<String, String> getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(Map<String, String> errorInfo) {
            this.errorInfo = errorInfo;
        }

    }
}
