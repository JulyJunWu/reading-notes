package com.ws.design.decorator;

/**
 * @author Jun
 * data  2019-09-21 12:50
 */
public abstract class Decorator implements Component {

    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    public Decorator() {
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    @Override
    public void perform() {
        component.perform();
    }
}
