package com.rain.net.netty.rpc.server;

import com.rain.net.netty.rpc.common.annotation.RpcService;
import com.rain.net.netty.rpc.common.api.HelloService;

/**
 * HelloServiceImpl
 *
 * @author lazy cat
 * 2020-08-20
 **/
@RpcService
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }
}
