package com.ws.design.strategy;

/**
 * @author Jun
 * data  2019-09-24 22:04
 */
public class Main {

    public static void main(String[] args) {

        Context context = new Context(new QuickSortStrategy());

        context.add(5);
        context.add(2);
        context.add(7);
        context.add(10);
        context.add(1);

        context.sort();
        context.println();

        context.add(111);
        context.add(0);

        context.setSortStrategy(new SystemSortStrategy());
        context.sort();
        context.println();

    }
}
