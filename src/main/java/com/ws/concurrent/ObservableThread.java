package com.ws.concurrent;

/**
 * @author Jun
 * data  2019-08-03 21:51
 */
public class ObservableThread<T> extends Thread implements Observable {

    private final TaskLifeCycle<T> lifeCycle;

    private final Task<T> task;

    private CycleEvent cycle;

    private T result;


    public ObservableThread(Task<T> task) {
        this(new TaskLifeCycle.EmptyLifeCycle<>(), task);
    }

    public ObservableThread(TaskLifeCycle<T> taskLifeCycle, Task<T> task) {
        super();
        this.lifeCycle = taskLifeCycle;
        this.task = task;
    }


    @Override
    public CycleEvent getCycleEvent() {
        return this.cycle;
    }

    private void update(CycleEvent cycle) {

        try {
            this.cycle = cycle;

            //通知订阅者
            lifeCycle.publicEvent(cycle);
        } catch (Exception e) {
            this.cycle = CycleEvent.ERROR;
            throw e;
        }
    }


    @Override
    public void run() {

        //根据线程执行的顺序依次触发对应的生命周期事件
        this.update(CycleEvent.STARTED);

        try {
            this.update(CycleEvent.RUNNING);

            //真正做事的方法
            T call = result = this.task.call();

            this.update(CycleEvent.DONG);

        } catch (Throwable e) {
            this.update(CycleEvent.ERROR);
            throw e;
        }
    }

    public T getResult() {
        return result;
    }
}
