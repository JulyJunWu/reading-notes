package com.ws.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Jun
 * data  2019-08-04 0:14
 */
public class ShareData {

    private List<Character> share;

    private ReadWriteLock readWriteLock = ReadWriteLock.readWriteLock(false);

    private Lock readLock = readWriteLock.readLock();

    private Lock writeLock = readWriteLock.writeLock();

    private int length;

    public ShareData(int capacity) {

        share = new ArrayList<>(capacity);
        length = capacity;
        IntStream.range(0, length).forEach(i -> {
            share.add(i, 'w');
        });
    }

    /**
     * 读操作
     * @return
     * @throws InterruptedException
     */
    public char[] read() throws InterruptedException {

        try {
            readLock.lock();

            char[] chars = new char[length];

            IntStream.range(0, share.size()).forEach(i -> {
                chars[i] = share.get(i);
            });

            slowly();
            return chars;
        } finally {
            readLock.unLock();
        }
    }

    private void slowly() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * 写操作
     * @param c
     * @throws InterruptedException
     */
    public void write(char c) throws InterruptedException {
        try {
            writeLock.lock();
            share.clear();
            IntStream.range(0, length).forEach(i -> {
                share.add(i, c);
            });

            slowly();
        } finally {
            writeLock.unLock();
        }
    }


}
