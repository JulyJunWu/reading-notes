package com.ws.design.iterator;

/**
 * @author Jun
 * data  2019-09-25 21:10
 */
public interface Iterator<T> {

    boolean hasNext();

    T next();

    void remove();

}
