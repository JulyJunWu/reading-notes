package com.ws.netty.nio;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Jun
 * data  2019-08-21 20:29
 * 文件映射 堆外内存
 * 将文件映射到堆外内存中,操作这些数据操作系统会直接写入文件中;
 */
public class TestNio8 {

    public static void main(String[] args) throws Exception{

       RandomAccessFile accessFile = new RandomAccessFile("ws.txt", "rw");

        FileChannel channel = accessFile.getChannel();

        //这个是直接内存
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        //用IDEA打开文件没发生变化,实际上已经发生改变了
        mappedByteBuffer.put(0, (byte) 'H');

        accessFile.close();


        Integer i = 10;
       System.out.println( i.equals(new Short((short) 10)));

    }

}
