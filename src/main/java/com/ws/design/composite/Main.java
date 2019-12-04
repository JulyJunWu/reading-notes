package com.ws.design.composite;

/**
 * @author Jun
 * data  2019-09-22 10:53
 * 客户端入口
 */
public class Main {

    public static void main(String[] args) {

        Component component = new Leaf();
        Component component2 = new Leaf();

        Composite composite = new Composite();
        composite.add(component);
        composite.add(component2);

        composite.println();
    }

}
