package com.ws.design.singleton;

/**
 * @author Jun
 * data  2019-09-17 0:11
 */
public class Singleton4 {

    /**
     * 进制指令重排序,防止创建时出现指令重排
     */
    private static volatile Singleton4 singleton4;

    private Singleton4() {
    }

    public static Singleton4 getSingleton() {
        if (null == singleton4) {
            synchronized (Singleton4.class) {
                if (null == singleton4) {
                    singleton4 = new Singleton4();
                }
            }
        }
        return singleton4;
    }
}
