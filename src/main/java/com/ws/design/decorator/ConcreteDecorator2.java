package com.ws.design.decorator;

/**
 * @author Jun
 * data  2019-09-21 13:00
 */
public class ConcreteDecorator2 extends Decorator {

    public ConcreteDecorator2(Component component){
        super(component);
    }

    @Override
    public void perform() {
        System.out.println("running");
        super.perform();
    }
}
