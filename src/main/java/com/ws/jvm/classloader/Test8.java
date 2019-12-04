package com.ws.jvm.classloader;

import java.sql.DriverManager;

/**
 * @author Jun
 * data  2019-08-10 23:00
 */
public class Test8 {

    public static void main(String[] args) throws Exception {


        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        //loadClass 并不会导致类进行初始化
        Class<?> aClass = classLoader.loadClass("com.ws.jvm.classloader.Test8P");
        System.out.println(aClass);

        System.out.println("---------------------------");

        //导致类初始化
        Class<?> aClass1 = Class.forName("com.ws.jvm.classloader.Test8P");
        System.out.println(aClass1);

    }

}

class Test8P {

    static {
        System.out.println("Test8P");
    }

}
