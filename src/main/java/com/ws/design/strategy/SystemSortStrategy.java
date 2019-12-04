package com.ws.design.strategy;

import java.util.List;

/**
 * @author Jun
 * data  2019-09-24 22:09
 */
public class SystemSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Integer> list) {
        list.sort((o1, o2) -> o1 - o2);
    }
}
