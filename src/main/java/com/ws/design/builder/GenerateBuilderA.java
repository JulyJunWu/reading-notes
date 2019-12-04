package com.ws.design.builder;

/**
 * @author Jun
 * data  2019-09-16 23:10
 */
public class GenerateBuilderA implements Builder {

    private Product product = new Product();

    @Override
    public Builder buildPartA() {
        product.add("part A");
        return this;
    }

    @Override
    public Builder buildPartB() {
        product.add("part B");
        return this;
    }

    @Override
    public Product getResult() {
        return product;
    }
}
