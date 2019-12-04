package com.ws.design.observer;

/**
 * @author Jun
 * data  2019-09-23 21:48
 */
public class ConcreteObserver implements IObserver {

    private String name;

    private String mood;

    private Subject subject;

    public ConcreteObserver(String name) {
        this.name = name;
    }

    @Override
    public void cancel() {
        subject.remove(this);
        System.out.println(name + "-> 累觉不爱,我放弃!");
    }

    @Override
    public void update(String mood) {
        this.mood = mood;
        System.out.println(name + mood);
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
