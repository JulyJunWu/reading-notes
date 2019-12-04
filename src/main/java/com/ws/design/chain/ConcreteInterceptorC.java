package com.ws.design.chain;

/**
 * @author Jun
 * data  2019-09-26 20:26
 */
public class ConcreteInterceptorC extends AbstracterHandleInterceptor {

    public void handle(Request request) {

        System.out.println(this.getClass().getSimpleName() + "开始");

        if (next != null) {
            next.handle(request);
        }

        System.out.println(this.getClass().getSimpleName() + "结束");

    }
}
