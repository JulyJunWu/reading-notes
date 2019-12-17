package com.ws.book.netty权威指南.messagepack;

import com.ws.book.netty权威指南.model.Message;
import com.ws.mybatis.util.HessianUtils;
import org.junit.Test;
import org.msgpack.MessagePack;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author JunWu
 * 测试序列化框架MessaePack
 */
public class MessagePackTest {

    @Test
    public void one() throws Exception {
        Message message = new Message();
        message.setAge(18);
        message.setName("念念不忘");
        message.setSex("男");

        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(message);

        Message encoder = new Message();
        pack.read(bytes, encoder);

        System.out.println(encoder);
    }

    /**
     * 序列化对比
     * <p>
     * 测试结果
     * project                  MessagePack          JDK              Hessian2
     * 序列化大小(b)               41               173                109
     * 次数/时间(100次/ms)        2145              25                 134
     * 次数/时间1000次/ms)        6159              30                 85
     * 次数/时间(10000次/ms)      44883             65                129
     */
    @Test
    public void compareSerializable() throws Exception {
        //数据
        Message message = new Message();
        message.setAddress("北京市朝阳区");
        message.setAge(18);
        message.setName("奋斗的念念");
        message.setSex("男");

        //MessagePack
        long startTime = System.currentTimeMillis();
        messagePack(message);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        //原生序列化
        startTime = System.currentTimeMillis();
        originalSerializable(message);
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        //Hessian
        startTime = System.currentTimeMillis();
        HessianUtils.serialize(message);
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

    public static int messagePack(Message message) throws Exception {
        MessagePack pack = new MessagePack();
        byte[] write = pack.write(message);
        return write.length;
    }

    public static int originalSerializable(Message message) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(outputStream);
        stream.writeObject(message);
        stream.flush();
        stream.close();
        byte[] bytes = outputStream.toByteArray();
        return bytes.length;
    }
}

