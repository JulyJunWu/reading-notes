package com.ws.design.template;

/**
 * @author Jun
 * data  2019-09-23 23:25
 */
public class ConcreteProcess extends  AbstractProcess {

    @Override
    void firstStep() {
        System.out.println("开始");
    }

    @Override
    void secondStep() {
        System.out.println("操作");

    }

    @Override
    void last() {
        System.out.println("结束");
    }
}
