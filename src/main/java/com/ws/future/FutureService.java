package com.ws.future;


/**
 * @author Jun
 * data  2019-08-04 11:43
 */
public interface FutureService<In,Out> {

    //无返回
    Future<?> submit(Runnable runnable);

    //有返回值
    Future<Out> submit(Task<In, Out> task, In input, CallBack<Out> callBack);

    static <T,R> FutureService<T,R> newService(){
        return new FutureServiceImpl<>();
    }

}
