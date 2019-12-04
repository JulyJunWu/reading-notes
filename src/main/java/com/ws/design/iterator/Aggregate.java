package com.ws.design.iterator;

/**
 * @author Jun
 * data  2019-09-25 21:36
 */
public interface Aggregate<T> {

    Iterator<T> createIterator();
}
