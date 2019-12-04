package com.ws.design.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * data  2019-09-23 21:46
 */
public class ConcreteSubject implements Subject {

    //存放所有的订阅者
    private List<IObserver> iObservers = new ArrayList<>();

    String mood;

    @Override
    public void register(IObserver iObserver) {
        iObservers.add(iObserver);
        iObserver.setSubject(this);
    }

    @Override
    public void update() {
        iObservers.stream().forEach(p -> p.update(this.mood));
    }

    @Override
    public void remove(IObserver iObserver) {
        iObservers.remove(iObserver);
    }

    public void changeMood(String mood) {
        this.mood = mood;
        System.out.println("暗恋对象心情:" + mood);
        update();
    }
}
