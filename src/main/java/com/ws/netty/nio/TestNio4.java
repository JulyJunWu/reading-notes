package com.ws.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Jun
 * data  2019-08-20 23:12
 */
public class TestNio4 {

    public static void main(String[] args) throws Exception {

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String src = loader.getResource("ws.txt").getPath();
        String dest = loader.getResource("tt.txt").getPath();

        try (FileInputStream fileInputStream = new FileInputStream(src);
             FileOutputStream fileOutputStream = new FileOutputStream(dest);) {

            FileChannel inputStreamChannel = fileInputStream.getChannel();

            FileChannel outputStreamChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while (inputStreamChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                outputStreamChannel.write(byteBuffer);

                //如果此行注释掉,会发生什么?? 死循环, 不断的将byteBuffer中的数据重复往文件中写入 ;
                //原因 : 不调用clear方法则position和limit相等,首先 position是不可能大于limit的,经过第二轮的读的时候,发现remain(剩余的空间) = limit - position = 0 , 导致已经没有任何空间可供读了,于是返回0;这样就不断的死循环了;
                byteBuffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
