package com.ws.design.builder;

/**
 * @author Jun
 * data  2019-09-16 23:14
 */
public class Main {
    public static void main(String[] args) {
        Director director = new Director();

        Builder builderA = new GenerateBuilderA();
        director.builder(builderA);
        Product product = builderA.getResult();
        product.show();

        Builder builderB = new GenerateBuilderB();
        director.builder(builderB);
        Product product2 = builderB.getResult();
        product2.show();
    }
}
