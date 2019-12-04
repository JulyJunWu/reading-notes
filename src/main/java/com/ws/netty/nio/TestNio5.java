package com.ws.netty.nio;

import java.nio.ByteBuffer;

/**
 * @author Jun
 * data  2019-08-20 23:29
 * 存放数据类型,怎么放就怎么取,参考JVM字节码
 */
public class TestNio5 {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putChar('c');
        buffer.putInt(66);
        buffer.putLong(88888888888888L);
        buffer.putShort(Short.MAX_VALUE);
        buffer.putFloat(1.0F);

        //必须读写转换
        buffer.flip();

        //取出顺序必须与写入数据一致;
        System.out.println(buffer.getChar());
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getFloat());
    }

}
