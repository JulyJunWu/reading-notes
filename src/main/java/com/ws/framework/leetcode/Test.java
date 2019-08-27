package com.ws.framework.leetcode;

public class Test {

    public static void main(String[] args) {
        f1();
    }

    static Test Test = new Test();

    static {
        System.out.println(1);
    }

    {
        System.out.println(2);
    }

    Test() {
        System.out.println(3);
        System.out.println("a=" + a + ",b =" + b);
    }

    public static void f1() {
        System.out.println(4);
    }

    int a = 100;

    static int b = 200;

}
