package com.ws.design.singleton;

/**
 * @author Jun
 * data  2019-09-17 0:01
 */
public class Singleton2 {

    private static Singleton2 singleton2;

    private Singleton2() {
    }

    /**
     * 线程不安全单例
     *
     * @return
     */
    public static Singleton2 getInstance() {

        if (singleton2 == null) {
            singleton2 = new Singleton2();
        }

        return singleton2;
    }
}
