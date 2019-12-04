package com.ws.design.observer;

/**
 * @author Jun
 * data  2019-09-23 21:43
 */
public interface IObserver {

    void cancel();

    void update(String mood);

    void setSubject(Subject subject);

}
