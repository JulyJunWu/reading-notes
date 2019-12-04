package com.ws.design.observer;

import java.util.Observer;

/**
 * @author Jun
 * data  2019-09-23 21:42
 */
public interface Subject {

    void register(IObserver iObserver);

    void update();

    void remove(IObserver iObserver);

}
