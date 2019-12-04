package com.ws.netty.nio;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Jun
 * data  2019-08-20 21:36
 * 传统输出流转为NIO
 */
public class TestNio3 {

    public static void main(String[] args) throws Exception {

        URL resource = ClassLoader.getSystemClassLoader().getResource("ws.txt");

        FileOutputStream fileOutputStream = new FileOutputStream(resource.getPath());

        //获取channel对象
        FileChannel channel = fileOutputStream.getChannel();
        //分配缓冲
        ByteBuffer allocate = ByteBuffer.allocate(1024);

        String str = "good,cute,love,nice,luck\n";
        //写入数据
        allocate.put(str.getBytes());
        //读写转换
        allocate.flip();
        //写入文本
        channel.write(allocate);
        //关闭资源
        fileOutputStream.close();

        channel.read(allocate);
    }

}
