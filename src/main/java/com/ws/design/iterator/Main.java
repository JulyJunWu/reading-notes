package com.ws.design.iterator;

/**
 * @author Jun
 * data  2019-09-25 21:19
 */
public class Main {


    public static void main(String[] args) {

        ConcreteAggregate<String> aggregate = new ConcreteAggregate<>();

        aggregate.push("Hello");
        aggregate.push("Good");
        aggregate.push("Nice");
        aggregate.push("Mark");

        Iterator<String> iterator = aggregate.createIterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
            iterator.remove();
        }

        aggregate.push("Hi");

        Iterator<String> iterator1 = aggregate.createIterator();
        while (iterator1.hasNext()) {
            String next = iterator1.next();
            System.out.println(next);
        }
    }

}
