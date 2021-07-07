package com.project.core.common.response;

/**
 * 返回结果
 *
 * @author lilj
 * @since May 05, 2020 16:13:50 PM
 */
public enum ReturnCode {

    /**
     * 请求失败
     */
    FAILED(0, "操作失败"),
    /**
     * 请求成功
     */
    SUCCESS(1, "操作成功");

    /**
     * 返回状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    ReturnCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
