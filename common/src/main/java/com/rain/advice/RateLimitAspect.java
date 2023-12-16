package com.rain.advice;

import com.google.common.util.concurrent.RateLimiter;
import com.rain.annotation.limit.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RateLimitAspect
 *
 * @author lazy cat
 * @since 2019-06-13
 **/
@Aspect
@Component
public class RateLimitAspect {

    //用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter)
    private final ConcurrentHashMap<String, RateLimiter> rateLimitMap = new ConcurrentHashMap<>();

    private final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    /**
     * 配置切面
     **/
    @Pointcut("execution(public * com.rain.controller.*.*(..))")
    public void limit() {
    }

    /**
     * 在所拦截方法前执行一段逻辑
     **/
    @Before("limit()")
    public void daBefore() {
    }

    /**
     * 在所拦截方法后执行一段逻辑
     **/
    @AfterReturning(pointcut = "limit()")
    public void doAfter() {
    }

    /**
     * Around注解可以在所拦截方法前后执行一段逻辑
     **/
    @Around("limit()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object result;

        log.info("limit aop...");

        // 没加注解 直接执行返回结果->point.proceed()
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        boolean hasAnnotation = method.isAnnotationPresent(RateLimit.class);
        if (!hasAnnotation) {
            return point.proceed();
        }

        RateLimit annotation = method.getAnnotation(RateLimit.class);

        String limitKey = annotation.limitKey();

        String limitCount = annotation.limitCount();

        if (!rateLimitMap.containsKey(limitKey)) {
            //使用最简洁的方法来创建RateLimiter，RateLimiter.create(double xx)，如果有需要，可自行设置RateLimiter其他属性
            rateLimitMap.put(limitKey, RateLimiter.create(Integer.parseInt(limitCount)));
        }

        RateLimiter rateLimiter = rateLimitMap.get(limitKey);

        if (rateLimiter.tryAcquire(1)) {
            result = point.proceed();
        } else {
            throw new Exception("服务限流!");
        }

        return result;
    }
}
