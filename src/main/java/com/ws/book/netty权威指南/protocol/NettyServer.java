package com.ws.book.netty权威指南.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author JunWu
 * 自定义协议栈服务端
 */
public class NettyServer {

    public void start() throws Exception {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            ChannelFuture future = bootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //解码(处理入栈请求)  参数1:最大长度 , 参数2
                            NettyMessageDecoder decoder = new NettyMessageDecoder(1024 * 1024, 4, 4);
                            decoder.setName("server");
                            pipeline.addLast("NettyMessageDecoder", decoder);
                            //编码(处理出栈请求)
                            NettyMessageEncoder encoder = new NettyMessageEncoder();
                            encoder.setName("server");
                            pipeline.addLast("NettyMessageEncoder", encoder);
                            //超时处理
                            pipeline.addLast("ReadTimeoutHandler", new ReadTimeoutHandler(50L, TimeUnit.SECONDS));
                            //登录认证响应
                            pipeline.addLast("LoginAuthResHandler", new LoginAuthResHandler());
                            //心跳响应
                            pipeline.addLast("HeartBeatResHandler", new HeartBeatResHandler());
                        }
                    }).bind(5566).sync();
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().start();
    }
}
