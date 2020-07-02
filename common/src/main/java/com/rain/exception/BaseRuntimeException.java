package com.rain.exception;


import com.rain.constant.ResultCode;

/**
 * 通用运行异常
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
public class BaseRuntimeException extends RuntimeException {

    private String code;

    private String msg;

    BaseRuntimeException(ResultCode resultCode) {
        super();
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
