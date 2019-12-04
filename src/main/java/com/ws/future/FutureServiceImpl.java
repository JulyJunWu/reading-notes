package com.ws.future;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jun
 * data  2019-08-04 11:47
 */
public class FutureServiceImpl<In, Out> implements FutureService<In, Out> {

    private final String THREAD_PREFIX_NAME = "ZWS-";

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    private CallBack callBack;

    private String getNextThreadName() {
        return THREAD_PREFIX_NAME + atomicInteger.getAndIncrement();
    }

    @Override
    public Future<?> submit(Runnable runnable) {

        FutureTask<Void> futureTask = new FutureTask<>();

        new Thread(() -> {
            runnable.run();
            futureTask.finish(null);
        }, getNextThreadName()).start();

        return futureTask;
    }

    @Override
    public Future<Out> submit(Task<In, Out> task, In input, CallBack<Out> callBack) {
        this.callBack = callBack;
        FutureTask<Out> futureTask = new FutureTask<>();

        new Thread(() -> {
            Out out = task.get(input);
            futureTask.finish(out);

            //回调
            if (callBack != null) {
                callBack.call(out);
            }

        }, getNextThreadName()).start();

        return futureTask;
    }
}
