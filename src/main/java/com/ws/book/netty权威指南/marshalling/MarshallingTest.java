package com.ws.book.netty权威指南.marshalling;

import com.ws.book.netty权威指南.model.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;

/**
 * @author JunWu
 * 无需担心粘包和拆包
 * 测试
 */
public class MarshallingTest {

    /**
     * 使用Marshalling进行编解码(序列化),
     * 对象必须实现Serializable接口
     *
     * @throws Exception
     */
    @Test
    public void marshallingServerTest() throws Exception {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            ChannelFuture future = serverBootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler()).childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(MarshallingFactory.createMarshallingDecoder());
                            pipeline.addLast(MarshallingFactory.createMarshallingEncoder());
                            pipeline.addLast(new ServerHandler());
                        }
                    }).bind(6688).sync();

            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    @Test
    public void marshallingClientTest() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture future = bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(MarshallingFactory.createMarshallingDecoder());
                            pipeline.addLast(MarshallingFactory.createMarshallingEncoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    }).connect("127.0.0.1", 6688).sync();

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}


class ServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            System.out.println(msg);
            ctx.writeAndFlush(msg);
        }
    }
}


class ClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            System.out.println(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message message = new Message();
        message.setSex("男");
        message.setName("宇宙无敌");
        message.setAge(28);
        message.setAddress("至高界银河系地球村");
        ctx.writeAndFlush(message);
    }
}
