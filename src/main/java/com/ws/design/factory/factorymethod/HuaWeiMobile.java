package com.ws.design.factory.factorymethod;

/**
 * @author Jun
 * data  2019-09-18 19:59
 */
public class HuaWeiMobile implements Mobile {
    @Override
    public void call() {
        System.out.println("使用华为手机打电话");
    }
}
