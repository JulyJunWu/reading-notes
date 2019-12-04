package com.ws.latch;

import java.util.concurrent.TimeUnit;

/**
 * @author Jun
 * data  2019-08-05 19:56
 */
public class CountDownLatch extends AbstractLatch {

    public CountDownLatch(int limit) {
        super(limit);
    }

    @Override
    public void await() throws InterruptedException {

        synchronized (this) {

            while (limit > 0) {
                this.wait();
            }

        }

    }

    @Override
    public void await(TimeUnit timeUnit, long timeout) throws InterruptedException {

        if (timeout <= 0)
            throw new RuntimeException("参数有误!");

        long remain = timeUnit.toNanos(timeout);
        long endTime = remain + System.nanoTime();

        synchronized (this) {

            while (limit > 0) {

                if (remain <= 0) {
                    throw new RuntimeException("任务已超时!!");
                }

                this.wait(TimeUnit.NANOSECONDS.toMillis(remain));

                remain = endTime - System.nanoTime();
            }

        }


    }

    @Override
    public void countDown() {

        synchronized (this) {

            while (limit <= 0) {
                throw new RuntimeException("参数异常");
            }

            System.out.println("未完成数量 -> " + (--limit));
            this.notifyAll();
        }

    }
}
