package com.ws.design.decorator;

/**
 * @author Jun
 * data  2019-09-21 12:49
 */
public class ConcreteComponent  implements Component{

    @Override
    public void perform() {
        System.out.println("sleep");
    }
}
