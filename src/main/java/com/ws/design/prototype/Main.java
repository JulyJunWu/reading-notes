package com.ws.design.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * data  2019-09-19 21:15
 * 原型模式
 *
 * 深拷贝和浅拷贝是直接对内存中的数据进行操作的,不会再次对构造器进行初始化的;
 * 这样的好处在于如果一个类的创建开销过大,可以通过这种方式来拷贝一个副本或者新的一个副本,用来节省系统的开销
 *
 */
public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {

        List<String> list = new ArrayList<>(2);
        list.add("Hello");
        list.add("World");
        Prototype prototype = new Prototype("WS", 18, 20, list);

        Prototype clone = (Prototype) prototype.clone();

        System.out.println(prototype.list == clone.list);
    }
}
