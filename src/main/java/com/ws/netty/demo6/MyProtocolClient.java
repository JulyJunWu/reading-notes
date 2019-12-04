package com.ws.netty.demo6;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.stream.IntStream;

/**
 * @author Jun
 * data  2019-09-05 22:17
 * 自定义协议客户端
 */
public class MyProtocolClient {


    public static void main(String[] args) {


        NioEventLoopGroup single = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap().group(single).channel(NioSocketChannel.class).handler(new MyProtocolChannelInitial());
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 8888)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            single.shutdownGracefully();
        }


    }

}

class MyProtocolChannelInitial extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyProtocolEncode());
        pipeline.addLast(new MyProtocolDecode());
        pipeline.addLast(new MyProtocolClientHandler());

    }
}

class MyProtocolClientHandler extends SimpleChannelInboundHandler<MyProtocol> {

    private int count;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            MyProtocol protocol = new MyProtocol();
            protocol.setContent("路漫漫其修远兮".getBytes("utf-8"));
            protocol.setLength(protocol.getContent().length);
            ctx.writeAndFlush(protocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyProtocol msg) throws Exception {
        System.out.println("来自服务端的数据:" + new String(msg.getContent(), Charset.defaultCharset()));
        System.out.println("接收的次数:" + (++count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}