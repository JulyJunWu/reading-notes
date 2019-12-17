package com.ws.book.netty权威指南;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Constructor;
import java.net.SocketAddress;

/**
 * @author JunWu
 * 创建netty服务
 */
public class NettyUtils {

    private NettyUtils() {
    }

    /**
     * 启动服务
     */
    public static void startNettyServer(int port, ChannelHandler[] channelHandlers, Object[] args, Object[] argsType) {
        NioEventLoopGroup boss = null;
        NioEventLoopGroup work = null;
        try {
            boss = new NioEventLoopGroup();
            work = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            ChannelFuture future = bootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            if (!ArrayUtils.isEmpty(channelHandlers)) {
                                ChannelPipeline pipeline = ch.pipeline();
                                int index = 0;
                                for (ChannelHandler channelHandler : channelHandlers) {
                                    Object[] params = (Object[]) args[index];
                                    Class[] aClass = (Class[]) argsType[index];
                                    Constructor<? extends ChannelHandler> constructor = channelHandler.getClass().getDeclaredConstructor(aClass);
                                    if (!constructor.isAccessible()) {
                                        constructor.setAccessible(true);
                                    }
                                    pipeline.addLast(constructor.newInstance(params));
                                    index++;
                                }
                            }
                        }
                    }).bind(port).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    /**
     * 开启netty客户端
     *
     * @param address
     * @param channelHandlers
     */
    public static void startNettyClient(SocketAddress address, ChannelHandler... channelHandlers) {

        NioEventLoopGroup group = null;
        try {
            group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture connect = bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(channelHandlers);
                        }
                    }).connect(address).sync();

            connect.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
