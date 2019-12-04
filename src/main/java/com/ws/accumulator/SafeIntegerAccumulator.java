package com.ws.accumulator;

/**
 * @author Jun
 * data  2019-08-04 10:24
 * 线程安全的可变对象累加器
 * 对象不可变,字段值不可变,每次累加的都是新的一个值;
 * <p>
 * 将class final修饰 , 变量也是用final修饰
 */
public final class SafeIntegerAccumulator {

    private final int init;


    public SafeIntegerAccumulator(int init) {
        this.init = init;
    }

    public SafeIntegerAccumulator(SafeIntegerAccumulator safeIntegerAccumulator, int init) {
        this.init = safeIntegerAccumulator.getInit() + init;
    }

    public SafeIntegerAccumulator add(int increase) {
        return new SafeIntegerAccumulator(this, increase);
    }

    public int getInit() {
        return init;
    }
}
