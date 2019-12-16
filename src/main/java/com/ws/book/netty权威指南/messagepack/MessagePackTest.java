package com.ws.book.netty权威指南.messagepack;

import org.junit.Test;
import org.msgpack.MessagePack;

/**
 * @author JunWu
 * 测试序列化框架MessaePack
 */
public class MessagePackTest {

    @Test
    public void one() throws Exception {
        MessagePackServer.Message message = new MessagePackServer.Message();
        message.setAge(18);
        message.setName("念念不忘");
        message.setSex("男");

        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(message);

        MessagePackServer.Message encoder = new MessagePackServer.Message();
        pack.read(bytes, encoder);

        System.out.println(encoder);
    }
}

