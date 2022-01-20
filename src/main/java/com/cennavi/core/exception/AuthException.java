package com.cennavi.core.exception;

import lombok.Getter;

/**
 * token 认证 异常
 */
@Getter
public class AuthException extends RuntimeException {
    private int code;

    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
