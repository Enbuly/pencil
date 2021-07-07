package com.rain.advice;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.rain.annotation.secret.SecretBody;
import com.rain.annotation.secret.SecretHttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 拦截需要解密的请求。
 * 注意：post请求且有@RequestBody注解才能拦截
 *
 * @author lazy cat
 * @since 2020-01-08
 **/
@RestControllerAdvice
@ConditionalOnProperty(prefix = "faster.secret", name = "enabled", havingValue = "true")
public class SecretRequestAdvice implements RequestBodyAdvice {

    @Value("${faster.secret.key}")
    private String salary;

    private final Logger log = LoggerFactory.getLogger(SecretRequestAdvice.class);

    //检查方法上是否存在SecretBody注解，存在即解密，否则不处理
    private boolean supportSecretRequest(MethodParameter parameter) {
        if (null != parameter.getMethod()) {
            return parameter.getMethod().isAnnotationPresent(SecretBody.class);
        }
        return false;
    }

    //解密
    private String decryptBody(HttpInputMessage inputMessage) throws IOException {

        byte[] key = salary.getBytes(StandardCharsets.UTF_8);
        AES aes = SecureUtil.aes(key);

        InputStream encryptStream = inputMessage.getBody();
        byte[] bytes = new byte[1024];
        int length = encryptStream.read(bytes);
        String result = new String(bytes, 0, length, StandardCharsets.UTF_8);
        log.info("hello secret...");
        log.info(encryptStream.toString());
        return aes.decryptStr(result);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        boolean supportSafeMessage = supportSecretRequest(parameter);

        String httpBody;
        if (supportSafeMessage) {
            httpBody = decryptBody(inputMessage);
        } else {
            httpBody = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
        }

        return new SecretHttpMessage(new ByteArrayInputStream(httpBody.getBytes()), inputMessage.getHeaders());
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    public static void main(String[] args) {
        String content = "{\n" +
                "\"id\": \"12\",\n" +
                "\"name\": \"zzy\",\n" +
                "\"salary\": 120,\n" +
                "\"status\": 0,\n" +
                "\"phone\": \"13828831312\",\n" +
                "\"password\": \"120\"\n" +
                "}";

        byte[] key = "1234567898765432".getBytes(StandardCharsets.UTF_8);

        AES aes = SecureUtil.aes(key);

        String encrypt = aes.encryptHex(content.replaceAll(" ", ""));

        System.out.println(encrypt);

        String decrypt = aes.decryptStr("7226558f28e0afe9493ec733d86279adaa84dac84eaacc6b63450015b158c156605e958b2dae5619928bc730196781ce69c615f17333060757ce8046539a885525b6e34c632c449a4cee94948a72823f281fab143cfb94977633aabc9b1f3ac1");

        System.out.println(decrypt);
    }
}
