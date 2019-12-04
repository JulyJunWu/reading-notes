package com.ws.design.adapter;

/**
 * @author Jun
 * data  2019-09-22 20:23
 */
public class ConcreteAdapter2 extends ConcreteAdaptee2 implements Target {
    @Override
    public void newWork() {
        oldWork();
    }
}
