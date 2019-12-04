package com.ws.design.adapter;

/**
 * @author Jun
 * data  2019-09-22 20:29
 */
public class Adapter implements Target {

    private Adaptee adaptee;

    public Adapter(Adaptee adaptee){
        this.adaptee = adaptee;
    }

    @Override
    public void newWork() {
        adaptee.oldWork();
    }
}
