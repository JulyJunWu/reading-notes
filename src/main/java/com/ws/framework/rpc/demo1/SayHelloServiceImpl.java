package com.ws.framework.rpc.demo1;

/**
 * @Description:
 * @Date: 2019/8/10 0010 11:14
 */
public class SayHelloServiceImpl implements ISayHelloService {
    @Override
    public String sayHello(String param) {
        return "Hello".equals(param) ? "Hello" : "error";
    }
}
