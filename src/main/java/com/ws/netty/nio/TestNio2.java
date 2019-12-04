package com.ws.netty.nio;


import org.junit.Test;

import java.io.FileInputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Jun
 * data  2019-08-20 21:24
 * 传统IO输入流转成NIO
 */
public class TestNio2 {

    public static void main(String[] args) throws Exception {

        URL resource = ClassLoader.getSystemClassLoader().getResource("ws.txt");
        FileInputStream fileInputStream = new FileInputStream(resource.getPath());

        FileChannel channel = fileInputStream.getChannel();

        //必须构建一个缓冲, 用于读写操作
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);

        //读取数据到缓冲
        channel.read(byteBuffer);

        //读写转换
        byteBuffer.flip();

        System.out.println(new String(byteBuffer.array()));

        //关闭资源
        fileInputStream.close();
    }
}
