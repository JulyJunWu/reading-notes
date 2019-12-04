package com.ws.design.factory.simplefactory;

/**
 * @author Jun
 * data  2019-09-18 23:42
 */
public class Main {

    public static void main(String[] args) {

        PersonFactory personFactory = new PersonFactory();
        Person teacher = personFactory.createInstance("teacher");
        teacher.work();

        teacher = personFactory.createInstance("student");
        teacher.work();
    }
}
