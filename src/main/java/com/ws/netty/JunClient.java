package com.ws.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description:
 * @Author: JulyJunWu
 * @Date: 2020/6/26 20:31
 */
@Slf4j
public class JunClient {

    public static List<Channel> ALL = new ArrayList<>();
    public static AtomicLong NUMBER = new AtomicLong(1);
    public static NioEventLoopGroup boss = new NioEventLoopGroup(3);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Bootstrap bootstrap = build();
            ChannelFuture connect = bootstrap.connect("localhost", 8888);
            ALL.add(connect.channel());
        }
        System.out.println("启动完成" + ALL.size());
        LockSupport.park();
    }

    public static Bootstrap build() {
        return new Bootstrap().group(boss).channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    private String name = "World" + NUMBER.getAndIncrement();

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
                        String content = byteBuf.toString(Charset.defaultCharset());
                        log.info("我是[{}],来自服务端数据->{}", name,content);
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer();
                        byteBuf.writeBytes(name.getBytes());
                        ctx.writeAndFlush(byteBuf);
                        //  定时发送
                        ctx.executor().scheduleAtFixedRate(() -> {
                            ByteBuf byteBuf2 = ByteBufAllocator.DEFAULT.directBuffer();
                            byteBuf2.writeBytes(name.getBytes());
                            ctx.writeAndFlush(byteBuf2);
                        }, 5, 10, TimeUnit.SECONDS);
                    }
                });
    }
}
