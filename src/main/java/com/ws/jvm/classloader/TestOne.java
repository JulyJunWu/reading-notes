package com.ws.jvm.classloader;

/**
 * @author Jun
 * data  2019-08-08 22:49
 * <p>
 * 常量在编辑期间会存入调用方法所在的类的常量池中
 * 本质上,调用类并没有直接引用定义常量所在类,
 * 所以不会初始化常量所在的类
 */
public class TestOne {

    public static void main(String[] args) {
        //只输出 18
        System.out.println(TestOnePlus.num);

        /**
         * 未主动使用TestB 却被加载了, 但是并未初始化
         */
        System.out.println(TestB.num);

        /**
         *  原因 :
         *      JVM允许类加载器 预料某个类将要使用前预先进行加载(并未初始化)
         *      若预先加载过程中.class文件出错,并不会直接报错,而是在该类首次初始化时才抛出错误(linkageError错误)
         *
         *      若该类一直未被主动使用,那么类加载器就不会报错;
         */
    }
}


class TestOnePlus {

    public static final int num = 18;

    static {
        System.out.println("TestTwo");
    }
}

/**
 * 测试 未主动首次进行的类却JVM被加载了
 */
class TestA{
    public static int num = 10;

    static {
        System.out.println("TestA");
    }
}

class TestB extends TestA{

    public static int num2 = 20;

    static {
        System.out.println("TestB");
    }

}
