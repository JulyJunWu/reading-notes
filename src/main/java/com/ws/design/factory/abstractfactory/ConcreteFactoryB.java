package com.ws.design.factory.abstractfactory;

/**
 * @author Jun
 * data  2019-09-18 21:25
 */
public class ConcreteFactoryB extends AbstractFactory {
    @Override
    AbstractProductA produceA() {
        return new ProductA2();
    }

    @Override
    AbstractProductB productB() {
        return new ProductB2();
    }
}
