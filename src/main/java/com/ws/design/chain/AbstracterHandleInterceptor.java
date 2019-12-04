package com.ws.design.chain;

/**
 * @author Jun
 * data  2019-09-26 20:21
 */
public abstract class AbstracterHandleInterceptor {

    protected AbstracterHandleInterceptor next;

    public abstract void handle(Request request);



}
