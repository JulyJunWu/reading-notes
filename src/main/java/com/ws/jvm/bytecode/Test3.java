package com.ws.jvm.bytecode;

/**
 * @author Jun
 * data  2019-08-17 11:21
 *
 * Grandpa f = new Father();
 * f 是静态类, 不会发生变, 而new Father()是实际类型,是可以发生变化, 多态特性 , 实际类型是在运行期间可确定的
 *
 *  重载是一个静态行为, 重写是动态行为
 */
public class Test3 {

    //方法重载是一种静态行为 , 静态类型编译期就可以确定
    public void test(Grandpa grandpa){
        System.out.println("grandpa");
    }

    public void test(Father father){
        System.out.println("father");
    }

    public void test(Son son){
        System.out.println("son");
    }

    public static void main(String[] args) {

        Test3 test3 = new Test3();

        Grandpa f = new Father();
        Grandpa s = new Son();

        test3.test(f);
        test3.test(s);

    }
}


class Grandpa {

}

class Father extends Grandpa {
}

class Son  extends  Father{
}
