package com.ws.design.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * data  2019-09-25 21:12
 */
public class ConcreteAggregate<T> implements Aggregate<T> {

    private List<T> list = new ArrayList<>();

    public void push(T t) {
        list.add(t);
    }

    public void remove(T t) {
        list.remove(t);
    }

    public Iterator<T> createIterator() {
        return new ConcreteIterator<T>();
    }

    class ConcreteIterator<T> implements Iterator {
        private volatile int concurrentIndex = 0;

        @Override
        public boolean hasNext() {
            return concurrentIndex < list.size();
        }

        @Override
        public T next() {
            return (T) list.get(concurrentIndex++);
        }

        @Override
        public void remove() {

            if (list.size() == 0 || concurrentIndex == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }

            list.remove(--concurrentIndex);
        }
    }


}
