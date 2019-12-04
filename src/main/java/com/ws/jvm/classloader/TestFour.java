package com.ws.jvm.classloader;

/**
 * @author Jun
 * data  2019-08-09 21:11
 * <p>
 * 调用接口中编译期间能确定的常量, 是不会触发该接口和父接口初始化(因为接口的变量都树常量,在编译期间直接保存在调用者类的常量池中)
 * <p>
 * 当调用接口中编辑期间不能确定的常量,则会对该接口进行初始化(若通过子接口调用父接口中编译期不能确定的常量,则父接口初始化,而非子接口)
 */
public class TestFour implements FourSon {

    public static void main(String[] args) {
        //System.out.println(FourSon.THREAD);
        //不会对父接口进行初始化
        TestFour four = new TestFour();
    }
}

/**
 * 接口中不能使用代码块
 */
interface FourParent {
    //接口中的变量默认都是(静态常量) public static final修饰的
    public static final int A = 10;

    Thread THREAD = new Thread() {
        {
            System.out.println("我是FourParent线程");
        }
    };

}

interface FourSon extends FourParent {
    public static final int B = 11;

    Thread THREAD2 = new Thread() {
        {
            System.out.println("我是FourSon线程");
        }
    };
}


