package com.ws.design.strategy;

import java.util.List;

/**
 * @author Jun
 * data  2019-09-24 21:55
 */
public class QuickSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Integer> list) {

        if (list == null || list.size() == 0) return;

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                Integer temp = list.get(j);
                if (temp > list.get(j + 1)) {
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}
