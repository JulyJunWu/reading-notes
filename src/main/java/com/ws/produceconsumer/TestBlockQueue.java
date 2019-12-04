package com.ws.produceconsumer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author Jun
 * data  2019-08-04 21:57
 * 生产者消费者案例
 */
public class TestBlockQueue {


    public static void main(String[] args) {

        AtomicInteger integer = new AtomicInteger(0);

        BlockQueue blockQueue = new BlockQueue();

        IntStream.range(0, 5).forEach(i -> new Thread(() -> {
            try {
                while (true) {
                    blockQueue.offer(integer.getAndIncrement());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());


        IntStream.range(0, 5).forEach(i -> new Thread(() -> {
            try {
                while (true) {
                    blockQueue.take();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start());

    }
}
