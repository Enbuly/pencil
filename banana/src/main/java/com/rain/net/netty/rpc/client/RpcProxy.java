package com.rain.net.netty.rpc.client;

import com.rain.net.netty.rpc.common.model.Request;
import com.rain.net.netty.rpc.common.model.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * RpcProxy
 *
 * @author lazy cat
 * @since 2020-08-20
 **/
@Component
public class RpcProxy {

    @Resource
    private RpcClient rpcClient;

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    Request request = new Request();
                    request.setId(UUID.randomUUID().toString());
                    request.setClassName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);

                    Response response = rpcClient.send(request, countDownLatch);

                    if (response.getCode() == 1) {
                        return response.getErrorMsg();
                    } else {
                        return response.getData();
                    }
                }
        );
    }
}
