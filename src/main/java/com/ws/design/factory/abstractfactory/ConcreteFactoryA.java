package com.ws.design.factory.abstractfactory;

/**
 * @author Jun
 * data  2019-09-18 21:20
 */
public class ConcreteFactoryA extends AbstractFactory {
    @Override
    AbstractProductA produceA() {
        return new ProductA1();
    }

    @Override
    AbstractProductB productB() {
        return new ProductB1();
    }
}
