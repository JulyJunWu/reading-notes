package com.ws.accumulator;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Jun
 * data  2019-08-04 10:34
 * 测试累加器
 */
public class TestAccumulator {


    public static void main(String[] args) {

        two();

    }


    public static void two() {
        SafeIntegerAccumulator safeIntegerAccumulator = new SafeIntegerAccumulator(10);

        IntStream.range(0, 10).forEach(i -> {
            new Thread(() -> {
                while (true) {
                    SafeIntegerAccumulator add = safeIntegerAccumulator.add(i);
                    int oldValue = safeIntegerAccumulator.getInit();

                    //安全
                    if (oldValue + i != add.getInit()) {
                        System.out.println("<<<所加结果对不上>>>");
                    }

                    sleep();
                }
            }).start();
        });
    }


    public static void sleep() {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void one() {
        UnSafeIntegerAccumulator unSafeIntegerAccumulator = new UnSafeIntegerAccumulator(10);

        IntStream.range(0, 10).forEach(i -> {

            new Thread(() -> {

                while (true) {
                    int oldValue, newValue;

                    //加锁得以保证安全
                    synchronized (unSafeIntegerAccumulator) {
                        oldValue = unSafeIntegerAccumulator.getInit();
                        newValue = unSafeIntegerAccumulator.add(i);
                    }

                    if (i + oldValue != newValue) {
                        System.out.println("<<<所加结果对不上>>>");
                    }

                    sleep();

                }
            }).start();

        });
    }
}
