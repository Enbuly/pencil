package com.rain.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 鉴权过滤器
 *
 * @author lazy cat
 * 2020-07-16
 **/
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Value("${auth.path}")
    private String authPath;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String[] strArray = authPath.split(",");
        List<String> list = Arrays.asList(strArray);

        if (list.contains(request.getURI().getPath())) {

            String token = request.getHeaders().getFirst("token");

            if (StringUtils.isEmpty(token)) {
                return authError(response);
            } else {
                String userName = stringRedisTemplate.opsForValue().get(token);
                if (!StringUtils.isEmpty(userName))
                    return chain.filter(exchange);
                else
                    return authError(response);
            }
        } else {
            return chain.filter(exchange);
        }
    }

    private Mono<Void> authError(ServerHttpResponse response) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String resultString = "token is not exist!";
        DataBuffer buffer = response.bufferFactory().wrap(resultString.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
