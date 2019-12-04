package com.ws.future;

/**
 * @author Jun
 * data  2019-08-04 11:42
 */
@FunctionalInterface
public interface Task<In, Out> {

    Out get(In input);
}
