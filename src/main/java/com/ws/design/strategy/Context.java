package com.ws.design.strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * data  2019-09-24 21:58
 */
public class Context {

    private SortStrategy sortStrategy;

    private List<Integer> list = new ArrayList<>();

    public Context(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public void sort() {
        if (sortStrategy != null) {
            sortStrategy.sort(list);
        }
    }

    public void add(int num) {
        list.add(num);
    }

    public void remove(int num) {
        list.remove(num);
    }

    public void println() {
        StringBuilder builder = new StringBuilder("[");
        list.stream().forEach(p -> builder.append(p).append(","));
        builder.delete(builder.length() - 1, builder.length());
        builder.append("]");
        System.out.println(builder);
    }

    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }
}
