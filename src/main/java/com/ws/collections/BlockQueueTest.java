package com.ws.collections;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author JunWu
 * 测试队列
 */
@Slf4j
public class BlockQueueTest {

    public static void main(String[] args) {
        System.out.println("<<<<<<<<GC>>>>>>>>");
    }

    @Test
    public void testQueue() {
        log.info("<<<<<<<<GC>>>>>>>>");
    }

    @Test
    public void testBlockQueue() throws Exception {

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue(1);

        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

        ThreadFactory threadFactory = new ThreadFactory() {
            private AtomicInteger num = new AtomicInteger();
            private String prefix = "ws";

            @Override
            public Thread newThread(Runnable r) {
                int i = num.incrementAndGet();
                Thread thread = new Thread(r);
                thread.setName(prefix + i);
                thread.setPriority(10);
                thread.setDaemon(false);
                return thread;
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 60L, TimeUnit.SECONDS, queue, threadFactory, handler);
        IntStream.range(0, 4).forEach(p -> executor.execute(() -> {
            try {
                System.out.println(Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        TimeUnit.SECONDS.sleep(2);

        Callable<String> callable = () -> {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName();
        };
        Future<String> future = executor.submit(callable);
        String result = future.get();
    }

    @Test
    public void testFutureTask() throws Exception {

        FutureTask<String> futureTask = new FutureTask<String>(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {

            }
            return Thread.currentThread().getName();
        });

        new Thread(futureTask).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(futureTask).start();
    }

}
