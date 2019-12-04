package com.ws.latch;

import java.util.concurrent.TimeUnit;

/**
 * @author Jun
 * data  2019-08-05 19:53
 */
public abstract class AbstractLatch {

    protected volatile int limit;

    public AbstractLatch(int limit) {
        this.limit = limit;
    }

    public abstract void await() throws InterruptedException;

    public abstract void await(TimeUnit timeUnit , long timeout) throws InterruptedException;

    public abstract void countDown();

    public int getUnArrived() {
        return limit;
    }

}
