package com.ws.design.decorator;

/**
 * @author Jun
 * data  2019-09-21 12:57
 * 装饰模式 :io也是装饰模式, 避免类的数量急剧膨胀
 */
public class Main {

    public static void main(String[] args) {

        Component component = new ConcreteComponent();
        Decorator decorator = new ConcreteDecorator(component);
        Decorator decorator2 = new ConcreteDecorator2(decorator);
        Decorator decorator3 = new ConcreteDecorator3(decorator2);
        decorator3.perform();

        System.out.println("------------------");

        decorator3.setComponent(component);
        decorator2.setComponent(decorator3);
        decorator.setComponent(decorator2);
        decorator.perform();
        System.out.println("------------------");

        decorator.setComponent(component);
        decorator2.setComponent(decorator);
        decorator3.setComponent(decorator2);
        decorator3.perform();

    }
}
