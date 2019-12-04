package com.ws.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author Jun
 * data  2019-08-03 22:01
 */
public class TestMain {


    public static void main(String[] args) throws Exception {


        ObservableThread<String> thread = new ObservableThread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "666";
        });

        thread.start();

        TimeUnit.SECONDS.sleep(1);

        CycleEvent cycle = thread.getCycleEvent();

        System.out.println(cycle);

        TimeUnit.SECONDS.sleep(4);

        String result = thread.getResult();

        System.out.println(result);

    }

}
