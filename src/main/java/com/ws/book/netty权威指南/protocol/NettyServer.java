package com.ws.book.netty权威指南.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author JunWu
 * 自定义协议栈服务端
 */
@Slf4j
public class NettyServer {

    public static long FREE_MEMORY;
    public static long TOTAL_MEMORY;
    public static long MAX_MEMORY;

    public void start() throws Exception {
        //不使用堆外内存
        // System.setProperty("io.netty.noPreferDirect","true");
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = FREE_MEMORY = runtime.freeMemory();
        long totalMemory = TOTAL_MEMORY = runtime.totalMemory();
        long maxMemory = MAX_MEMORY = runtime.maxMemory();
        log.info("begin start netty server || totalMemory[{}],maxMemory[{}],freeMemory[{}]", new Object[]{totalMemory, maxMemory, freeMemory});
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup(1);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            ChannelFuture future = bootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //解码(处理入栈请求)  参数1:最大长度 , 参数2:表示消息长度的开始位置 参数3:表示消息长度的结束位置
                            pipeline.addLast("NettyMessageDecoder", new NettyMessageDecoder("server", 1024 * 1024, 4, 4));
                            //编码(处理出栈请求)
                            pipeline.addLast("NettyMessageEncoder", new NettyMessageEncoder("server"));
                            //超时处理
                            pipeline.addLast("ReadTimeoutHandler", new ReadTimeoutHandler(50L, TimeUnit.SECONDS));
                            //登录认证响应
                            pipeline.addLast("LoginAuthResHandler", new LoginAuthResHandler());
                            //心跳响应
                            pipeline.addLast("HeartBeatResHandler", new HeartBeatResHandler());
                        }
                    }).bind(5566).sync();
            long remainMemory = runtime.freeMemory();
            // 使用了多少字节内存
            long usageB = TOTAL_MEMORY - remainMemory;
            // 使用了多少K内存
            long usageK = usageB / 1024;
            // 使用了多少M内存
            long usageM = usageK / 1024;
            freeMemory = remainMemory;
            totalMemory = runtime.totalMemory();
            maxMemory = runtime.maxMemory();
            log.info("after netty server start|| totalMemory[{}],maxMemory[{}],freeMemory[{}],使用字节数[{}].使用K数[{}],使用M数[{}]", new Object[]{totalMemory, maxMemory, freeMemory, usageB, usageK, usageM});
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
