package com.ws.design.builder;

import javax.swing.*;

/**
 * @author Jun
 * data  2019-09-16 23:19
 */
public class GenerateBuilderB implements Builder {
    private Product product = new Product();

    @Override
    public Builder buildPartA() {
        product.add("hello A");
        return this;
    }

    @Override
    public Builder buildPartB() {
        product.add("hello B");
        return this;
    }

    @Override
    public Product getResult() {
        return product;
    }
}
