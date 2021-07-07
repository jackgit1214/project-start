/**
 *
 */
package com.project.core.common.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lilj
 *
 */
@Slf4j
@Data
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer errorCode;
    private String errorMsg;

    public BusinessException() {
        super();
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

}
