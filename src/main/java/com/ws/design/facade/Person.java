package com.ws.design.facade;

/**
 * @author Jun
 * data  2019-09-21 21:59
 */
public class Person {
    private String name;
    private int age;

    public Person(String name , int age){
        this.name = name;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
