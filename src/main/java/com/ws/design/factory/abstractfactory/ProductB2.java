package com.ws.design.factory.abstractfactory;

/**
 * @author Jun
 * data  2019-09-18 21:18
 */
public class ProductB2 extends AbstractProductB {
    @Override
    void perform(AbstractProductA productA) {
        System.out.println("我是产品" + this.getClass().getSimpleName());
        productA.work();
    }
}
