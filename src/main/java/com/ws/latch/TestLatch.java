package com.ws.latch;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Jun
 * data  2019-08-05 20:02
 */
public class TestLatch {


    public static void main(String[] args) throws Exception {

        CountDownLatch downLatch = new CountDownLatch(5);

        SecureRandom secureRandom = new SecureRandom();

        IntStream.range(0, 5).forEach(i -> new Thread(() -> {
            try {

                TimeUnit.SECONDS.sleep(secureRandom.nextInt(20));

            } catch (Exception e) {
                e.printStackTrace();
            }
            downLatch.countDown();
        }).start());

        try {
            //超时后,子线程并不会终止任务,需要自己掌控
            downLatch.await(TimeUnit.SECONDS, secureRandom.nextInt(20));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //System.exit(-1);
        }

        System.out.println("完毕!");

    }
}
