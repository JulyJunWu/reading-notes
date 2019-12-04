package com.ws.netty.demo6;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Jun
 * data  2019-09-05 21:58
 * 自定义协议
 */
public class MyProtocolServer {

    public static void main(String[] args) {


        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap().group(boss, work).channel(NioServerSocketChannel.class).childHandler(new MyChannelInitial());
            ChannelFuture sync = bootstrap.bind(8888).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

}


class MyChannelInitial extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyProtocolDecode());
        pipeline.addLast(new MyProtocolEncode());

        pipeline.addLast(new MyProtocolServerHandler());


    }
}

/**
 * 继承ReplayingDecoder,我们不必去关心可读取的字节是否足够
 */
class MyProtocolDecode extends ReplayingDecoder<Void> {

    private int count;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("解码");

        int length = in.readInt();

        byte[] bytes = new byte[length + 1];
        //字节数必须与ByteBuf可读的大小一致,否则后续解析后面的数据必然报错
        in.readBytes(bytes);

        MyProtocol protocol = new MyProtocol();
        protocol.setLength(length);
        protocol.setContent(bytes);

        System.out.println("长度:" + length);
        System.out.println("次数:" + (++count));

        out.add(protocol);
    }
}

class MyProtocolEncode extends MessageToByteEncoder<MyProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyProtocol msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}

class MyProtocolServerHandler extends SimpleChannelInboundHandler<MyProtocol> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyProtocol msg) throws Exception {

        System.out.println("接受来自客户端的数据: " + new String(msg.getContent(), Charset.defaultCharset()));

        byte[] bytes = "吾将上下而求索".getBytes(Charset.defaultCharset());
        MyProtocol protocol = new MyProtocol();
        protocol.setLength(bytes.length);
        protocol.setContent(bytes);
        ctx.writeAndFlush(protocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}