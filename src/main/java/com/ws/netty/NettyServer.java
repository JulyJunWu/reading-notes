package com.ws.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.Charset;

/**
 * @author JunWu
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitial());

        ChannelFuture channelFuture = bootstrap.bind(8888);
        channelFuture.channel().closeFuture().sync();
    }
}

/**
 * 辅助注册handler类
 */
class ChannelInitial extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ResponseHandler());
    }
}

class ResponseHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
        //  此处无需释放ByteBuf,其父类会处理释放问题
        System.out.println(msg.toString(Charset.defaultCharset()));
        Channel channel = channelHandlerContext.channel();
        channel.writeAndFlush(Unpooled.wrappedBuffer("收到!".getBytes()));
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(1);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(2);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(3);
        super.channelInactive(ctx);
    }
}