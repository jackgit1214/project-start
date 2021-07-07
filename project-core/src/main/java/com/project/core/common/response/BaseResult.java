package com.project.core.common.response;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

/**
 * 返回结果
 *
 * @author lilj
 * @since May 05, 2020 16:13:50 PM
 */
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 4241583739802575733L;

    /**
     * 返回状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public BaseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
