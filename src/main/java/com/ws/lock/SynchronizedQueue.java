package com.ws.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @author JunWu
 * 手写简易阻塞队列
 */
@Slf4j
public class SynchronizedQueue {

    private ReentrantLock lock;
    /**
     * 通知生产者生产数据 条件
     */
    private Condition putCondition;
    /**
     * 唤醒消费者消费数据 条件
     */
    private Condition takeCondition;
    private volatile Node head = new Node(null, null);
    private volatile Node tail = head;

    public SynchronizedQueue() {
        lock = new ReentrantLock();
        takeCondition = lock.newCondition();
        putCondition = lock.newCondition();
    }

    /**
     * @param v
     * @return
     */
    public boolean putQueue(Object v) {
        lock.lock();
        try {
            Node node = new Node(v, null);
            if (head == tail) {
                tail = node;
                head.next = tail;
            } else {
                tail.next = node;
                tail = node;
            }
        } finally {
            takeCondition.signal();
            lock.unlock();
        }
        return true;
    }

    private void reset(Node node) {
        head.next = node.next;
        node.next = null;
        if (node == tail) {
            tail = head;
        }
    }

    public Object pollQueue() {
        lock.lock();
        try {
            while (head == tail || head.next == null) {
                log.info("无数据可消费,线程[{}]等待唤醒", Thread.currentThread().getName());
                takeCondition.await();
            }
            Node next = head.next;
            reset(next);
            return next.v;
        } catch (Exception e) {
            return null;
        } finally {
            lock.unlock();
        }
    }

    private static class Node {
        private Object v;
        private Node next;

        Node(Object v, Node next) {
            this.next = next;
            this.v = v;
        }
    }

    public static void main(String[] args) throws Exception {
        SynchronizedQueue queue = new SynchronizedQueue();

        IntStream.range(0, 100).forEach(p -> queue.putQueue(p));

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Object data;
                while ((data = queue.pollQueue()) != null) {
                    log.info("线程[{}]抢到[{}]", Thread.currentThread().getName(), data);
                }
            }, i + "号").start();
        }

        TimeUnit.SECONDS.sleep(5);

        IntStream.range(300, 400).forEach(p -> queue.putQueue(p));

        LockSupport.park();
    }
}
