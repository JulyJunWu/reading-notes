package com.ws.design.builder;

/**
 * @author Jun
 * data  2019-09-16 23:12
 * 指挥者对象,屏蔽具体构造的过程,对构建过程进行封装
 */
public class Director {

    public void builder(Builder builder) {
        builder.buildPartA().buildPartB();
    }

}
