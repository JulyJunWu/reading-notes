package com.ws.netty.nio;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

/**
 * @author Jun
 * data  2019-08-21 0:01
 * <p>
 * 只读缓冲
 */
public class TestNio7 {

    public static void main(String[] args) {

        ByteBuffer allocate = ByteBuffer.allocate(1024);
        System.out.println(allocate.getClass());

        IntStream.range(0, 100).forEach(p -> {
            allocate.put((byte) p);
        });


        //转为只读Buffer,底层是HeapByteBufferR
        ByteBuffer buffer = allocate.asReadOnlyBuffer();
        System.out.println(buffer.getClass());

        //所有的put操作都将报错
        //buffer.put((byte) 1);

    }
}
