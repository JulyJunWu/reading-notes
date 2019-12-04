package com.ws.produceconsumer;

import java.util.LinkedList;

/**
 * @author Jun
 * data  2019-08-04 21:43
 * <p>
 * 仓库
 */
public class BlockQueue {


    private static final int MAX_SIZE = 100;

    private LinkedList<Integer> list = new LinkedList<>();

    public void offer(int i) throws Exception {

        synchronized (list) {

            while (list.size() >= MAX_SIZE) {
                list.wait();
            }
            list.addLast(i);
            System.out.println("生产数据 -> " + i + "当前数量 -> " + list.size());
            list.notifyAll();
        }
    }

    public void take() throws Exception {

        synchronized (list) {

            while (list.isEmpty()) {
                list.wait();
            }

            int result = list.removeFirst();
            System.out.println("消费数据 -> " + result + " 当前数量 -> " + list.size());

            list.notifyAll();
        }
    }


}
