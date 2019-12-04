package com.ws.design.decorator;

/**
 * @author Jun
 * data  2019-09-21 19:43
 */
public class ConcreteDecorator3 extends Decorator {

    public ConcreteDecorator3(Component component){
        super(component);
    }

    @Override
    public void perform() {
        super.perform();
        System.out.println("wake up");
    }
}
