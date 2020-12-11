package com.ws.jmx;

import javax.management.MXBean;

@MXBean
public interface IShop {

    String getAddress();

    void setAddress(String address);

    String getPrice();

    void setPrice(String price);
}
