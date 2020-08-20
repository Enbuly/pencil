package com.rain.net.netty.rpc.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request
 *
 * @author lazy cat
 * 2020-08-13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    private String id;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;
}
