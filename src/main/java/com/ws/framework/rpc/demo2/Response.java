package com.ws.framework.rpc.demo2;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Date: 2019/8/10 0010 14:11
 */
@Data
@AllArgsConstructor
public class Response {

    private byte encode;

    private String response;
}
