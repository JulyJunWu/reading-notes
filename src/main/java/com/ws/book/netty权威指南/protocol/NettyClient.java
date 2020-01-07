package com.ws.book.netty权威指南.protocol;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author JunWu
 * 自定义协议栈客户端
 */
@Slf4j
public class NettyClient {

    private ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, threadFactoryBuilder.setNameFormat("ws-netty-client-%d").build());

    private NioEventLoopGroup group = new NioEventLoopGroup();

    private volatile int retry;
    private volatile int maxRetry;

    public void connect(String host, int port) throws Exception {

        Bootstrap bootstrap = new Bootstrap();
        try {
            ChannelFuture future = bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //消息解码
                            pipeline.addLast("NettyMessageDecoder", new NettyMessageDecoder("client", 1024 * 1024, 4, 4));
                            //消息编码
                            pipeline.addLast("NettyMessageEncoder", new NettyMessageEncoder("client"));
                            //登录认证处理
                            pipeline.addLast("LoginAuthReqHandler", new LoginAuthReqHandler());
                            //心跳处理
                            pipeline.addLast("HeartBeatReqHandler", new HeartBeatReqHandler());
                        }
                    }).connect(host, port).sync();

            future.channel().closeFuture().sync();
        } finally {
            ++retry;
            log.info("客户端程序重新创建,时间[{}],重试次数[{}]", LocalDateTime.now(), retry);
            //重新创建一个新客户端
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    try {
                        connect(host, port);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 启动客户端
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new NettyClient().connect("127.0.0.1", 5566);
    }
}
