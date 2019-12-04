package com.ws.design.builder;

/**
 * @author :Jun
 * date : 2019-09-16 23:01
 */
public interface Builder {

    Builder buildPartA();

    Builder buildPartB();

    Product getResult();

}
