package com.ws.concurrent;

/**
 * @author :Jun
 * date : 2019-08-03 21:45
 */
public interface Observable {

    CycleEvent getCycleEvent();

    void start();

    void interrupt();
}
