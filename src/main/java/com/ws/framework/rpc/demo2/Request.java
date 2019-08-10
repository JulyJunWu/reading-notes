package com.ws.framework.rpc.demo2;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Date: 2019/8/10 0010 14:07
 * 封装请求
 */
@Data
@AllArgsConstructor
public class Request {

    private byte encode;

    private String command;

}
