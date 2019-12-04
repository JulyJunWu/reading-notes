package com.ws.netty.nio;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author Jun
 * data  2019-08-21 20:42
 * 文件锁
 */
public class TestNio9 {

    public static void main(String[] args) throws Exception{

        RandomAccessFile accessFile = new RandomAccessFile("ws.txt", "rw");

        FileChannel channel = accessFile.getChannel();

        //锁定的位置 , 是否共享锁
        FileLock lock = channel.lock(0, 5, true);

        System.out.println(lock.isShared());
        //释放锁
        lock.release();
        accessFile.close();
    }


}
