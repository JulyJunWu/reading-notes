package com.ws.book.netty权威指南;

import com.ws.book.netty权威指南.messagepack.MessagePackClient;
import com.ws.book.netty权威指南.messagepack.MessagePackServer;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author JunWu
 * netty测试
 */
public class NettyTest {

    /**
     * 测试使用
     * LengthFieldBasedFrameDecoder  用于对进栈数据解码
     * LengthFieldPrepender          用于对出栈数据进行编码
     * <p>
     * 用于自定义消息长度 + 数据
     */
    @Test
    public void testLength() throws Exception {
        Class<? extends ChannelHandler>[] aClass = new Class[]{LengthFieldBasedFrameDecoder.class, MessagePackServer.MsgPackDecoder2.class};
        Object[] args = new Object[]{null, new Object[]{65535, 0, 2, 0, 2}, null, null, null};
        Object[] argsType = new Object[]{null, new Class[]{int.class, int.class, int.class, int.class, int.class}, null, null, null};

        new Thread(() -> NettyUtils.startNettyServer(6666, aClass, args, argsType)).start();

        TimeUnit.SECONDS.sleep(2);
        NettyUtils.startNettyClient(new InetSocketAddress(6666), new LengthFieldPrepender(2), new MessagePackClient.SendMessageHandler2());
    }
}
