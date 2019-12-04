package com.ws.lock;

/**
 * @author :Jun
 * date : 2019-08-03 23:40
 */
public interface Lock {

    void lock() throws InterruptedException;

    void unLock();

}
