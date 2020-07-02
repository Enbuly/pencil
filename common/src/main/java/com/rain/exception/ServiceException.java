package com.rain.exception;

import com.rain.constant.ResultCode;

/**
 * 服务层异常
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
public class ServiceException extends BaseRuntimeException {

    public ServiceException(ResultCode resultCode) {
        super(resultCode);
    }
}
