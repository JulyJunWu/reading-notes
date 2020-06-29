package com.ws.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: JulyJunWu
 * @Date: 2020/6/26 20:17
 */
@Slf4j
public class JunServer {


    public static void main(String[] args)throws Exception {
        NioEventLoopGroup BOSS = new NioEventLoopGroup(1);
        NioEventLoopGroup WORKER = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(BOSS, WORKER).channel(NioServerSocketChannel.class)
                .childHandler(new MyHandler());

        ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
        channelFuture.channel().closeFuture().sync();
    }


    @ChannelHandler.Sharable
    public static class MyHandler extends SimpleChannelInboundHandler<ByteBuf>{
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
            String content = byteBuf.toString(Charset.defaultCharset());
            printAddress(ctx.channel());
            log.info("content:{}", content);
            ctx.writeAndFlush(ByteBufUtil.writeUtf8(ctx.alloc(),"Hello"));
        }
    }
    /**
     * 打印IP + port
     */
    public static void printAddress(Channel channel) {
        SocketAddress remoteAddress = channel.remoteAddress();
        SocketAddress localAddress = channel.localAddress();
        printAddress(remoteAddress);
        printAddress(localAddress);
    }

    public static void printAddress(SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            log.info("hostName[{}],hostString[{}],port[{}]", new Object[]{inetSocketAddress.getHostName(), inetSocketAddress.getHostString(), inetSocketAddress.getPort()});
        }
    }
}
