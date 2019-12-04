package com.ws.netty.demo3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author Jun
 * data  2019-08-18 20:51
 * 心跳检测
 * <p>
 * 客户端使用demo2的client测试即可!
 */
public class MyServerHeartbeat {

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.channel(NioServerSocketChannel.class)
                    .group(boss, work)
                    .childHandler(new MyServerHeartbeatInitializer())
                    //增加work处理器打印日志
                    .handler(new LoggingHandler());

            ChannelFuture future = serverBootstrap.bind(8888).sync();
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}

class MyServerHeartbeatInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //添加心跳检查处理器 三种状态检测: 读空闲 , 写空闲 , 以及读写空闲
        //参数都是超时时间
        pipeline.addLast(new IdleStateHandler(5, 10, 15, TimeUnit.SECONDS));

        pipeline.addLast(new MyServerHandler());

    }
}


class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {

            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            String message = null;

            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    message = "读空闲";
                    break;
                case WRITER_IDLE:
                    message = "写空闲";
                    break;
                case ALL_IDLE:
                    message = "读写都空闲";
                    break;
            }

            System.out.println(message);

            ctx.close();
        }
        super.userEventTriggered(ctx, evt);
    }
}