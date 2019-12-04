package com.ws.design.decorator;

/**
 * @author Jun
 * data  2019-09-21 12:52
 */
public class ConcreteDecorator extends Decorator {

    public ConcreteDecorator(Component component){
        super(component);
    }

    @Override
    public void perform() {
        System.out.println("washing");
        super.perform();
    }
}
