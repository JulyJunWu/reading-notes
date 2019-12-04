package com.ws.design.factory.abstractfactory;

/**
 * @author Jun
 * data  2019-09-18 21:21
 */
public class Client {

    private AbstractProductA productA;
    private AbstractProductB productB;

    public Client(AbstractFactory abstractFactory) {
        productA = abstractFactory.produceA();
        productB = abstractFactory.productB();
    }

    public void run() {
        productB.perform(productA);
    }

}
