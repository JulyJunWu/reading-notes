package com.ws.concurrent;

/**
 * @author :Jun
 * date : 2019-08-03 21:50
 * 真正执行任务方法 , 解决线程不能返回结果的问题
 *
 * 参考 {@link java.util.concurrent.Callable}
 */
@FunctionalInterface
public interface Task<T> {

    T call();
}
