package com.ws.netty.nio;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

/**
 * @author Jun
 * data  2019-08-20 23:40
 * slice后的新Buffer 与 原有Buffer底层共享 同一份数据 , 也就是改动一处,另一个也随之改变
 */
public class TestNio6 {


    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(16);

        IntStream.range(0, 16).forEach(p -> buffer.put((byte) p));

        buffer.position(5);
        buffer.limit(10);
        //返回一个新的buffer,但是底层还是共享的
        ByteBuffer slice = buffer.slice();

        //把新的Buffer每个乘以2 , 重新赋值 , 看看旧的会不会发生改变
        IntStream.range(0, slice.limit()).forEach(p -> {
            byte b = slice.get();
            slice.put(p, b *= 2);
        });

        buffer.position(0);
        buffer.limit(buffer.capacity());

        //打印可知, 原Buffer也发生改变了
        IntStream.range(0, buffer.capacity()).forEach(p -> {
            System.out.println(buffer.get());
        });


    }
}
