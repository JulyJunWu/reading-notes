package com.ws.future;

import java.util.concurrent.TimeUnit;

/**
 * @author :Jun
 * date : 2019-08-04 11:40
 */
public interface Future<T> {

    //获取返回值
    T get() throws InterruptedException;

    //是否执行成功
    boolean isDone();

    /**
     * beyond the wait seconds , throw e ;
     * @throws InterruptedException
     */
    T get(long timeout, TimeUnit timeUnit) throws Exception;

    boolean cancel();

}
