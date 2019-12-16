package com.ws.book.netty权威指南;

import com.ws.book.netty权威指南.messagepack.MessagePackClient;
import com.ws.book.netty权威指南.messagepack.MessagePackServer;
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
     * LengthFieldBasedFrameDecoder
     * LengthFieldPrepender
     * <p>
     * 用于自定义消息长度 + 数据
     */
    @Test
    public void testLength() throws Exception {
        LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder = new LengthFieldBasedFrameDecoder(65525, 0, 2, 0, 2);
        MessagePackServer.MsgPackDecoder2 msgPackDecoder2 = new MessagePackServer.MsgPackDecoder2();
        new Thread(
                () -> {
                    NettyUtils.startNettyServer(6666, lengthFieldBasedFrameDecoder, msgPackDecoder2
                    );
                }).start();

        TimeUnit.SECONDS.sleep(2);

        NettyUtils.startNettyClient(new InetSocketAddress(6666), new LengthFieldPrepender(2), new MessagePackClient.SendMessageHandler2());
    }


}
