package com.rain.controller;

import com.alibaba.druid.util.StringUtils;
import com.rain.constant.ResultCode;
import com.rain.exception.ParamsCheckException;
import com.rain.requestVo.PageRequestVo;

/**
 * 基础controller
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
public class BaseController {

    protected void checkToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new ParamsCheckException(ResultCode.PARAMETER_ERROR);
        }
    }

    protected void checkName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new ParamsCheckException(ResultCode.PARAMETER_ERROR);
        }
    }

    protected void checkPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new ParamsCheckException(ResultCode.PARAMETER_ERROR);
        }
    }

    protected void checkPageRequestVo(PageRequestVo pageRequestVo) {
        if (pageRequestVo.getCurrentPage() <= 0) {
            throw new ParamsCheckException(ResultCode.PARAMETER_ERROR);
        }
        if (pageRequestVo.getPageSize() <= 0) {
            throw new ParamsCheckException(ResultCode.PARAMETER_ERROR);
        }
    }
}
