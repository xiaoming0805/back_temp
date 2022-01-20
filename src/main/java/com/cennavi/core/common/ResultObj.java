package com.cennavi.core.common;

import io.swagger.annotations.ApiModelProperty;

/**
 * 封装返回值类
 */
public class ResultObj<T> {
    public static final int SUCCESS = 200;
    public static final int FAUlT = 500;
    @ApiModelProperty(value = "错误码", example = "200")
    private int code;
    @ApiModelProperty("提示信息")
    private String message;
    @ApiModelProperty("数据")
    private T data;

    public ResultObj() {

    }

    public ResultObj(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
