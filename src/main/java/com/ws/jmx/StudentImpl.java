package com.ws.jmx;

public class StudentImpl implements StudentMXBean {

    private String name;

    private int age;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getAge() {
        return age;
    }
}
