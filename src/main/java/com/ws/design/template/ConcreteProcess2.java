package com.ws.design.template;

/**
 * @author Jun
 * data  2019-09-23 23:39
 */
public class ConcreteProcess2 extends AbstractProcess {
    @Override
    void firstStep() {
        System.out.println(1);
    }

    @Override
    void secondStep() {
        System.out.println(2);
    }

    @Override
    void last() {
        System.out.println(3);
    }
}
