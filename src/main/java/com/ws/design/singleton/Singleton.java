package com.ws.design.singleton;

/**
 * @author Jun
 * <p>
 * data  2019-09-16 23:57
 * 第一步 : 私有化构造器
 * 第二部 : 构建私有静态实例;
 * 第三部 : 提供一个静态方法以供外部访问
 *
 *
 * 线程安全的单例模式 -> 类的初始化来保证线程安全
 */
public class Singleton {

    private static Singleton singleton = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return singleton;
    }


}
