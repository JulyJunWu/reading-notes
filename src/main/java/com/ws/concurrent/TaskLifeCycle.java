package com.ws.concurrent;

/**
 * @author :Jun
 * date : 2019-08-03 21:47
 */
public interface TaskLifeCycle<T> {

    /**
     * 通知订阅者
     * @param cycleEvent
     */
    void publicEvent(CycleEvent cycleEvent);


    class EmptyLifeCycle<T> implements TaskLifeCycle<T> {

        @Override
        public void publicEvent(CycleEvent cycleEvent) {

            //根据生命周期做判断 , 做一些逻辑
            System.out.println(cycleEvent);
        }
    }

}
