package com.ws.design.composite;

/**
 * @author Jun
 * data  2019-09-22 10:50
 */
public class Leaf implements Component {
    @Override
    public void println() {
        System.out.println("叶子节点" + this.getClass().getName());
    }
}
