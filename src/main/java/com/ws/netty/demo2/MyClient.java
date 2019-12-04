package com.ws.netty.demo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @author Jun
 * data  2019-08-18 19:01
 */
public class MyClient {


    public static void main(String[] args) throws Exception {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).handler(new MyClientChannelInitializer()).channel(NioSocketChannel.class);

            ChannelFuture future = bootstrap.connect("localhost", 8888);
            boolean close = false;
            Scanner scanner = new Scanner(System.in);
            while (!close) {
                try {
                    boolean active = future.channel().isActive();
                    String next = scanner.next();
                    if (active) {
                        future.channel().writeAndFlush(next);
                    }
                } catch (Exception e) {
                    close = true;
                }
            }

            future.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}

class MyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

        pipeline.addLast(new LengthFieldPrepender(4));

        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));

        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

        pipeline.addLast(new MyClintHandler());

    }
}

class MyClintHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
        //ctx.writeAndFlush("from client : 收到!");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //连接建立向服务器发送用户名 , 只会在建立连接时发送一次;
        ctx.writeAndFlush("叶凡");
        super.channelActive(ctx);
    }
}