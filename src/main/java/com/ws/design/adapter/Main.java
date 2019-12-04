package com.ws.design.adapter;

import org.junit.Test;

/**
 * @author Jun
 * data  2019-09-22 20:23
 */
public class Main {

    /**
     * 使用继承的方式实现适配器
     * 确定 : 无法动态切换 , 需要写过多的适配器
     */
    @Test
    public  void extendsType() {

        Target target = new ConcreteAdapter();
        target.newWork();


        Target target2 = new ConcreteAdapter2();
        target2.newWork();

    }


    /**
     * 使用组合方式实现适配器
     * 只需要一个适配器即可
     */
    @Test
    public void useComposite(){

        Adaptee adaptee = new ConcreteAdaptee();
        Target adapter = new Adapter(adaptee);
        adapter.newWork();

        Target adapter2 = new Adapter(new ConcreteAdaptee2());
        adapter2.newWork();


    }
}
