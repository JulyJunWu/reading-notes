package com.ws.book.netty权威指南.messagepack;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.msgpack.MessagePack;

import java.net.InetSocketAddress;
import java.util.stream.IntStream;

/**
 * @author JunWu
 * netty使用MessagePack
 */
public class MessagePackClient {

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture future = bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new SendMessageHandler());
                    }
                }).connect(new InetSocketAddress("127.0.0.1", 8888));

        future.channel().closeFuture().sync();
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
                    MessagePackServer.Message message = new MessagePackServer.Message();
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
}
