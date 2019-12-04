package com.ws.design.template;

/**
 * @author Jun
 * data  2019-09-23 23:26
 */
public class Main {

    public static void main(String[] args) {

        Client client = new Client(new ConcreteProcess());
        client.run();

        client.setAbstractProcess(new ConcreteProcess2());
        client.run();

    }

}
