package com.ws.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @author Jun
 * data  2019-09-02 20:30
 */
public class ByteBufTest {


    @Test
    public void test() {

        ByteBuf byteBuf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(byteBuf.readByte());
        }
    }

    @Test
    public void test2() {

        //生成的容量是根据 编码的后的容量 * 该编码最大的字节数
        // UTF-8 : 5 * 3.0 = 15 , 在UTF-8编码中,一个汉字占用三个字节
        //ISO-8859-1 = 5 * 1.0 = 5 , 在ISO-8859-1,一个汉字占用1个字节
        ByteBuf byteBuf = Unpooled.copiedBuffer("你好,世界", Charset.forName("UTF-8"));

        //15
        System.out.println(byteBuf.capacity());
        System.out.println(byteBuf.writerIndex());
        System.out.println(byteBuf.readerIndex());

        // writeIndex - readIndex;
        System.out.println(byteBuf.readableBytes());
        //  capacity - writeIndex
        System.out.println(byteBuf.writerIndex());

        System.out.println(byteBuf.toString(Charset.forName("UTF-8")));

    }

    /**
     * 复合缓冲区
     */
    @Test
    public void test3() {
        //获取一个复合缓冲对象
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();

        ByteBuf byteBuf = Unpooled.buffer(10);
        ByteBuf directBuffer = Unpooled.directBuffer(8);


        compositeByteBuf.addComponents(byteBuf, directBuffer);

        compositeByteBuf.forEach(System.out::println);


    }


}
