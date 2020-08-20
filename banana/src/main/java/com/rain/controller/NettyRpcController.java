package com.rain.controller;

import com.rain.net.netty.rpc.client.RpcProxy;
import com.rain.net.netty.rpc.common.api.HelloService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * NettyRpcController
 *
 * @author lazy cat
 * @since 2020-08-20
 **/
@Api(description = "Netty Rpc Controller")
@RestController
@RequestMapping(value = "/netty")
public class NettyRpcController {

    @Resource
    private RpcProxy rpcProxy;

    @GetMapping(value = "nettyRpc")
    public String nettyRpc(@RequestParam String name) {
        HelloService helloService = rpcProxy.create(HelloService.class);
        return helloService.hello(name);
    }
}
