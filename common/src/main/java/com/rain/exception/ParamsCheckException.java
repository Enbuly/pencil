package com.rain.exception;

import com.rain.constant.ResultCode;

/**
 * 请求包校验异常
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
public class ParamsCheckException extends BaseRuntimeException {

    public ParamsCheckException(ResultCode resultCode) {
        super(resultCode);
    }
}
