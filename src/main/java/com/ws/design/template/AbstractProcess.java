package com.ws.design.template;

/**
 * @author Jun
 * data  2019-09-23 23:23
 */
public abstract class AbstractProcess {

    abstract void firstStep();

    abstract void secondStep();

    abstract void last();

    public void process() {
        firstStep();
        secondStep();
        last();
    }

}
