package com.cennavi.core.exception;

import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class GlobalException extends RuntimeException {
    private int code;

    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
