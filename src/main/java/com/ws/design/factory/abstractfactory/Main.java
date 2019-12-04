package com.ws.design.factory.abstractfactory;

/**
 * @author Jun
 * data  2019-09-18 21:22
 */
public class Main {

    public static void main(String[] args) {

        AbstractFactory factoryA = new ConcreteFactoryA();
        Client client = new Client(factoryA);
        client.run();

        AbstractFactory factoryB = new ConcreteFactoryB();
        Client client2 = new Client(factoryB);
        client2.run();
    }

}
