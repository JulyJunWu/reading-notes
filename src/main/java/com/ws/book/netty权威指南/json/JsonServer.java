package com.ws.book.netty权威指南.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JunWu
 * 使用json解码器
 */
public class JsonServer {

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup boss = null;
        NioEventLoopGroup work = null;

        try {
            boss = new NioEventLoopGroup(1);
            work = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            ChannelFuture channelFuture = bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new JsonObjectDecoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ParseStringToObjectDecoder());
                        }
                    }).bind(8888).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}

@Slf4j
class ParseStringToObjectDecoder extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        assert msg != null;
        JSONObject object = JSON.parseObject(msg);
        log.info("json数据:[{}]", object);
    }
}