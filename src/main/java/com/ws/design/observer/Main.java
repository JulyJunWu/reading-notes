package com.ws.design.observer;

/**
 * @author Jun
 * data  2019-09-23 21:49
 */
public class Main {

    public static void main(String[] args) {

        IObserver observer = new ConcreteObserver("ws");
        IObserver observer2 = new ConcreteObserver("bb");
        IObserver observer3 = new ConcreteObserver("cb");


        ConcreteSubject subscribe = new ConcreteSubject();
        subscribe.register(observer);
        subscribe.register(observer2);
        subscribe.register(observer3);

        subscribe.changeMood("心情不好");

        observer.cancel();

        subscribe.changeMood("棒棒哒!");

    }

}
