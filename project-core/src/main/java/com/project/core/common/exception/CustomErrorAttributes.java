package com.project.core.common.exception;

import java.util.Map;

import com.project.core.common.response.BaseResult;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author lilj
 * @date 2021/06/17
 **/
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
		BaseResult result = (BaseResult) webRequest.getAttribute("res", 0);
		errorAttributes.put("errorData", result);
		return errorAttributes;//返回最终map
	}

}

