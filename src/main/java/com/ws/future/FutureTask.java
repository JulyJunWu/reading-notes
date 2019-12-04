package com.ws.future;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Jun
 * data  2019-08-04 11:50
 */
public class FutureTask<T> implements Future<T> {

    private T result;

    private volatile boolean isDone;

    private final Object LOCK = new Object();

    private volatile Thread thread;

    @Override
    public T get() throws InterruptedException {

        synchronized (LOCK) {

            while (!isDone) {
                LOCK.wait();
            }
            return result;
        }
    }

    protected void finish(T result) {

        synchronized (LOCK) {

            if (isDone)
                return;

            this.result = result;
            isDone = true;
            LOCK.notifyAll();
        }

    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public T get(long timeout, TimeUnit timeUnit) throws TimeoutException, InterruptedException {

        synchronized (LOCK) {

            long timeOut = System.currentTimeMillis() + timeUnit.toMillis(timeout);

            while (!isDone) {

                long remain = timeOut - System.currentTimeMillis();

                if (remain <= 0 && !isDone) {
                    throw new TimeoutException("time out");
                }

                LOCK.wait(remain);
            }
            return result;
        }
    }

    @Override
    public boolean cancel() {

        synchronized (LOCK) {
            if (!isDone && !thread.isInterrupted()) {
                thread.interrupt();
                return true;
            }
        }

        return false;
    }

}
