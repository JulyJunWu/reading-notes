package com.ws.design.factory.factorymethod;

/**
 * @author Jun
 * data  2019-09-18 20:00
 */
public class XiaoMiMobileFactory implements MobileFactory {
    @Override
    public Mobile createMobile() {
        System.out.println("生产小米手机");
        return new XiaoMiMobile();
    }
}
