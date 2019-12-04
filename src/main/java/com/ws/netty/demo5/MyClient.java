package com.ws.netty.demo5;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author Jun
 * data  2019-09-04 22:38
 */
public class MyClient {


    public static void main(String[] args) {

        NioEventLoopGroup group = new NioEventLoopGroup();


        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ClientInitialChannel());

        bootstrap.connect(new InetSocketAddress("localhost", 8888));
    }
}

class ClientInitialChannel extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyByteToLongDecode());

        pipeline.addLast(new MyLongToByteEncode());

        pipeline.addLast(new ClientHandler());

    }
}

class ClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("来自服务端数据 " + msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(6666L);
    }
}