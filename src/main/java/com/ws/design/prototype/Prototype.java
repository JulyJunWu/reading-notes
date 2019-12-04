package com.ws.design.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * data  2019-09-19 21:08
 * 深拷贝和浅拷贝
 * <p>
 * 深 : 拷贝基本数据类型 以及 深度拷贝引用类型(不与原对象共享引用对象)
 * 浅 : 拷贝基本数据类型,与原对象共享引用对象;
 */
public class Prototype implements Cloneable {

    String name;
    Integer age;
    int max;

    List<String> list;

    public Prototype(String name, Integer age, int max, List<String> list) {
        System.out.println("执行init...");
        this.name = name;
        this.age = age;
        this.max = max;
        this.list = list;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        //浅拷贝 , 拷贝的包装类型也是共享的
        Prototype prototype = (Prototype) super.clone();

        //深拷贝,拷贝引用数据,地址发生改变
        prototype.list = (List<String>) ((ArrayList) list).clone();
        return prototype;
    }

}
