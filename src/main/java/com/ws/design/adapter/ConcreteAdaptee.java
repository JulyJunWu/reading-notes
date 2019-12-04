package com.ws.design.adapter;

/**
 * @author Jun
 * data  2019-09-22 20:18
 */
public class ConcreteAdaptee implements Adaptee {
    @Override
    public void oldWork() {
        System.out.println("work hard 1");
    }
}
