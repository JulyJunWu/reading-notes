package com.ws.future;

/**
 * @author Jun
 * data  2019-08-04 17:37
 */
@FunctionalInterface
public interface CallBack<T> {

    /**
     * 回调接口
     * @param t
     */
    void call(T t);


}
