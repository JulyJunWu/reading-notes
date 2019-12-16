package com.ws.book.netty权威指南.messagepack;

import com.ws.book.netty权威指南.NettyUtils;
import com.ws.book.netty权威指南.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.msgpack.MessagePack;

import java.net.InetSocketAddress;
import java.util.stream.IntStream;

/**
 * @author JunWu
 * netty使用MessagePack
 */
public class MessagePackClient {

    public static void main(String[] args) throws Exception {
        NettyUtils.startNettyClient(new InetSocketAddress("127.0.0.1", 6666), new SendMessageHandler());
    }

    /**
     * 这边只是连接建立成功后发送消息而已
     */
    public static class SendMessageHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            MessagePack messagePack = new MessagePack();
            IntStream.range(0, 10).forEach(p -> {
                try {
                    Message message = new Message();
                    message.setAge(p);
                    message.setName("netty");
                    message.setSex("男");
                    message.setAddress("北京朝阳区");
                    byte[] write = messagePack.write(message);
                    ByteBuf buffer = ctx.alloc().buffer(4 + write.length);
                    buffer.writeInt(write.length);
                    buffer.writeBytes(write);
                    ctx.writeAndFlush(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 这边只是连接建立成功后发送消息而已
     */
    public static class SendMessageHandler2 extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            MessagePack messagePack = new MessagePack();
            IntStream.range(0, 10).forEach(p -> {
                try {
                    Message message = new Message();
                    message.setAge(p);
                    message.setName("netty");
                    message.setSex("男");
                    message.setAddress("北京朝阳区");
                    byte[] write = messagePack.write(message);
                    ByteBuf buffer = ctx.alloc().buffer( write.length);
                    buffer.writeBytes(write);
                    ctx.writeAndFlush(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
