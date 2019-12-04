package com.ws.design.factory.factorymethod;

/**
 * @author Jun
 * data  2019-09-18 20:01
 */
public class Main {

    public static void main(String[] args) {

        MobileFactory factory = new HuaWeiMobileFactory();
        Mobile mobile = factory.createMobile();
        mobile.call();


        factory = new XiaoMiMobileFactory();
        mobile = factory.createMobile();
        mobile.call();

    }
}
