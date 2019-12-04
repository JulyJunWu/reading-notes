package com.ws.jvm.classloader;

import java.util.UUID;

/**
 * @author Jun
 * data  2019-08-09 20:26
 * 当常量在编译期间不确定的时候,并不会加载到调用者类的常量池中
 * 主动使用常量类 , 因此常量所在类会进行初始化
 */
public class TestTwo {

    public static void main(String[] args) {
        //未初始化TestTwoPlus
        System.out.println(TestTwoPlus.age);
        //初始化TestTwoPlus
        System.out.println(TestTwoPlus.str);
    }
}

class TestTwoPlus {

    public static final String str = UUID.randomUUID().toString();

    public static final int age = 10;

    static {
        System.out.println("TestTwoPlus");
    }
}
