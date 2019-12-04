package com.ws.accumulator;

/**
 * @author Jun
 * data  2019-08-04 10:24
 * 线程不安全的可变对象累加器
 */
public class UnSafeIntegerAccumulator {

    private int init;


    public UnSafeIntegerAccumulator(int init) {
        this.init = init;
    }

    public int add(int increase) {
        return this.init += increase;
    }

    public int getInit() {
        return init;
    }
}
