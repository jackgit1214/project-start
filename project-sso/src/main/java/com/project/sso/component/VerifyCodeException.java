package com.project.sso.component;

import org.springframework.security.core.AuthenticationException;

/**
 * 声明一个验证码异常，用于抛出特定的验证码异常
 *
 * @author xiyang.ycj
 * @since Jul 09, 2019 11:43:25 AM
 */
public class VerifyCodeException extends AuthenticationException {
    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public VerifyCodeException(String msg) {
        super(msg);
    }
}
