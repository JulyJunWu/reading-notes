package com.ws.netty.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jun
 * data  2019-08-18 18:50
 * <p>
 * 基于netty的多人聊天室;
 */
public class MyServer {

    public static void main(String[] args) throws Exception {


        //默认使用当前电脑核数的线程数量 * 2 (Runtime.getRuntime().availableProcessors() * 2)
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerChannelInitializer());

            ChannelFuture future = serverBootstrap.bind(8888).sync();

            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}


class MyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

        pipeline.addLast(new LengthFieldPrepender(4));

        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

        pipeline.addLast(new MyServerHandler());

    }
}

class MyServerHandler extends SimpleChannelInboundHandler<String> {

    //广播可以使用 ChannelGroup netty自带的所有的channel(用户)都保存在这里 , 内置了广播机制
    //private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static Map<Channel, String> channelHashMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        boolean first = false;
        if (!channelHashMap.containsKey(ctx.channel())) {
            channelHashMap.put(ctx.channel(), msg);
            first = true;
        }

        String name = channelHashMap.get(ctx.channel());

        if (first) {
            System.out.println(name + "上线了!");
        } else {
            //服务器打印内容
            System.out.println(ctx.channel().remoteAddress() + " " + name + "说:" + msg);
        }

        //广播给所有人
        Set<Map.Entry<Channel, String>> entries = channelHashMap.entrySet();
        Iterator<Map.Entry<Channel, String>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Map.Entry<Channel, String> next = iterator.next();
            Channel channel = next.getKey();

            if (first && channel != ctx.channel()) {
                channel.writeAndFlush("玩家[" + name + "] 上线了");
            } else if (!first) {
                if (channel == ctx.channel()) {
                    channel.writeAndFlush("我说:" + msg);
                } else {
                    channel.writeAndFlush(name + "说:" + msg);
                }
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //关闭通道,断开与客户端连接
        channelHashMap.remove(ctx.channel());
        ctx.close();
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * 可以重写handlerRemoved方法中移除
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //将缓存中的连接移除
        Channel channel = ctx.channel();
        String name = channelHashMap.get(channel);
        channelHashMap.remove(channel);

        Iterator<Map.Entry<Channel, String>> iterator = channelHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Channel, String> next = iterator.next();
            if (next.getKey() != channel) {
                next.getKey().writeAndFlush("玩家[" + name + "]下线了!");
            }
        }
        super.channelUnregistered(ctx);
    }
}