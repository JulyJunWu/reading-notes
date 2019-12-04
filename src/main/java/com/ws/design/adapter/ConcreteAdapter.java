package com.ws.design.adapter;

/**
 * @author Jun
 * data  2019-09-22 20:20
 */
public class ConcreteAdapter extends ConcreteAdaptee implements Target  {

    @Override
    public void newWork() {
        oldWork();
    }
}
