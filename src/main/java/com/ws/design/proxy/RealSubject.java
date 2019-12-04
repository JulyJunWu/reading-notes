package com.ws.design.proxy;

/**
 * @author Jun
 * data  2019-09-22 19:02
 */
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("房东租房");
    }
}
