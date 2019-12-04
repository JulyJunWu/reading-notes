package com.ws.design.factory.abstractfactory;

/**
 * @author Jun
 * data  2019-09-18 21:16
 */
public class ProductA1 extends AbstractProductA {

    @Override
    void work() {
        System.out.println("我是产品" + this.getClass().getSimpleName());
    }
}
