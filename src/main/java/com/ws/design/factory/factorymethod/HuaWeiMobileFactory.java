package com.ws.design.factory.factorymethod;

/**
 * @author Jun
 * data  2019-09-18 20:01
 */
public class HuaWeiMobileFactory implements MobileFactory {
    @Override
    public Mobile createMobile() {
        System.out.println("生产华为手机");
        return new HuaWeiMobile();
    }
}
