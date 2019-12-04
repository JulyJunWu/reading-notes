package com.ws.lock;

import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;

/**
 * @author Jun
 * data  2019-08-04 0:27
 * <p>
 * <p>
 * 读写锁使用场景:
 * 建议使用 {@link StampedLock} -> lock家族的宠儿(摘自Java高并发编程详解);
 */
public class TestShareData {

    public static String text = "thisistestsharethread";

    public static void main(String[] args) {

        ShareData shareData = new ShareData(50);

        new Thread(() -> {
            IntStream.range(0, text.length()).forEach(i -> {
                try {
                    char c = text.charAt(i);
                    shareData.write(c);
                    System.out.println(currentThread() + " write -> " + c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }).start();


        IntStream.range(0, 10).forEach(i -> {

            new Thread(() -> {
                try {
                    while (true) {
                        char[] read = shareData.read();
                        String s = new String(read);
                        System.out.println(new String(read));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

    }
}
