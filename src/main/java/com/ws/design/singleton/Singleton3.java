package com.ws.design.singleton;

/**
 * @author Jun
 * data  2019-09-17 0:03
 */
public class Singleton3 {

    private static Singleton3 singleton3;

    private Singleton3() {
    }

    /**
     * 双重验证单例模式其实也是不安全的单例模式
     * 因为 singleton3 = new Singleton3(); 会被拆分成三个步骤
     * 1.分配内存空间;
     * 2.创建对象
     * 3.将singleton3指向内存空间;
     * 如果是按照此顺序,那么是没有问题的 , 但是一旦发生指令重排序就会出问题,如1 , 3 ,2 ,外层 直接判断已经不为null了 , 实际上还没有创建;
     *
     * 解决办法就是防止指令重排徐 : 使用volatile
     *
     * @return
     */
    public static Singleton3 getSingleton() {

        if (null == singleton3) {
            synchronized (Singleton3.class) {
                if (null == singleton3) {
                    singleton3 = new Singleton3();
                }
            }
        }
        return singleton3;
    }

}
