package com.ws.jmx;

/**
 * 测试JMX 以注解为主
 */
public class ShopImpl implements IShop {

    private String address;

    private String price;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
