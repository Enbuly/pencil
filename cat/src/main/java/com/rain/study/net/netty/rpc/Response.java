package com.rain.study.net.netty.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response
 *
 * @author lazy cat
 * 2020-08-13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String requestId;

    private int code;

    private String errorMsg;

    private Object data;
}
