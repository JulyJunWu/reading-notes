package com.ws.design.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * data  2019-09-16 23:07
 */
public class Product {

    private List<String> content = new ArrayList<>();

    public void add(String part) {
        content.add(part);
    }

    public void show() {
        System.out.println("show product");
        for (String s : content) {
            System.out.println(s);
        }
    }

}
