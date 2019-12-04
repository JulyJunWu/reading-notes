package com.ws.jvm.classloader;

/**
 * @author Jun
 * data  2019-08-09 20:57
 * new一个数组并不会导致该类初始化
 * 数组的实例是由JVM在运行期间是生成的
 *
 * new一个引用数组指令 -> anewarray  -> L[
 * new一个原始类型数组的指令 -> newarray  -> I[ / B[ / Z[
 *
 */
public class TestThree {

    public static void main(String[] args) {
        TestThreePlus[] testThreePluses = new TestThreePlus[10];

        //打印 class [Lcom.ws.jvm.classloader.TestThreePlus;
        System.out.println(testThreePluses.getClass());

        //打印 class java.lang.Object
        System.out.println(testThreePluses.getClass().getSuperclass());
    }
}

class TestThreePlus {

    static {
        System.out.println("TestThreePlus");
    }
}
