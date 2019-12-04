package com.ws.future;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Jun
 * data  2019-08-04 12:02
 */
public class TestFuture {

    public static void main(String[] args) throws Exception {

        //one();
        //two();


        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));


        java.util.concurrent.Future<?> submit = poolExecutor.submit(() -> {

            try {

                while (true) {
                    System.out.println("我很正常");
                }
            } catch (Exception e) {
                System.out.println("我被打断了,emmmm");
            }

        });

        TimeUnit.SECONDS.sleep(4);

        boolean cancel = submit.cancel(true);

    }

    public static void one() throws Exception {
        FutureService<Void, Void> futureService = FutureService.newService();

        Future<?> future = futureService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("complete");
        });


        Object result = future.get();

        System.out.println("返回结果 -> " + result);
    }


    public static void two() throws Exception {
        FutureService<String, Integer> futureService = FutureService.newService();

        Future<Integer> future = futureService.submit(input -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return input.length();
        }, "ZWS", out -> {
            System.out.println("回调接口 : " + out);
        });

        TimeUnit.SECONDS.sleep(1);

        Integer result = future.get(3, TimeUnit.SECONDS);


        System.out.println("返回结果 -> " + result);
    }

}
