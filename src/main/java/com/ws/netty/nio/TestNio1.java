package com.ws.netty.nio;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

/**
 * @author Jun
 * data  2019-08-20 21:08
 */
public class TestNio1 {
    public static void main(String[] args) {


        IntBuffer allocate = IntBuffer.allocate(10);

        //写入
        IntStream.range(0,10).forEach(p-> allocate.put(p));

        allocate.flip();

        //一个字节一个字节读取
        while (allocate.hasRemaining()){
            System.out.println(allocate.get());
        }
    }
}
