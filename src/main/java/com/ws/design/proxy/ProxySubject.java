package com.ws.design.proxy;

/**
 * @author Jun
 * data  2019-09-22 19:02
 */
public class ProxySubject implements Subject {

    private RealSubject realSubject;

    public ProxySubject(RealSubject realSubject) {
        this.realSubject = realSubject;
    }


    @Override
    public void request() {
        this.beforeRequest();
        realSubject.request();
        this.afterRequest();
    }

    private void beforeRequest() {
        System.out.println("收取中介费");
    }

    private void afterRequest() {
        System.out.println("收取房东中介费");
    }
}
