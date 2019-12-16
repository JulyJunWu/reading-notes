package com.ws.book.netty权威指南.messagepack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.Data;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author JunWu
 * netty使用MessagePack序列化POJO
 */
public class MessagePackServer {

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture future = serverBootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ParseByteBufHandler());
                        pipeline.addLast(new MsgPackDecoder());
                    }
                }).bind(8888);

        future.channel().closeFuture().sync();
    }

    public static class ParseByteBufHandler extends ReplayingDecoder {
        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            int length = byteBuf.readInt();
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            list.add(bytes);
        }
    }

    public static class MsgPackDecoder extends SimpleChannelInboundHandler<byte[]> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
            MessagePack pack = new MessagePack();
            Message message = new Message();
            pack.read(bytes, message);
            System.out.println(message);
        }
    }

    @org.msgpack.annotation.Message
    @Data
    public static class Message {
        private String name;
        private String sex;
        private int age;
        private String address;
    }
}
