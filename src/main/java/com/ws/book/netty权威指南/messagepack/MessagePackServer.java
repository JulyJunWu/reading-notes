package com.ws.book.netty权威指南.messagepack;

import com.ws.book.netty权威指南.NettyUtils;
import com.ws.book.netty权威指南.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ReplayingDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author JunWu
 * netty使用MessagePack序列化POJO
 */
public class MessagePackServer {

    public static void main(String[] args) throws Exception {
        NettyUtils.startNettyServer(6666,new ChannelHandler[]{ new ParseByteBufHandler(), new MsgPackDecoder()},null,null);
    }

    public static class ParseByteBufHandler extends ReplayingDecoder {
        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            int length = byteBuf.readInt();
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            list.add(bytes);
        }
    }

    /**
     * 使用MessagePack将byte[]解析成POJO
     */
    public static class MsgPackDecoder extends SimpleChannelInboundHandler<byte[]> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
            MessagePack pack = new MessagePack();
            Message message = new Message();
            pack.read(bytes, message);
            System.out.println(message);
        }
    }

    /**
     * 使用MessagePack将ByteBuf解析成POJO
     */
    public static class MsgPackDecoder2 extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            MessagePack messagePack = new MessagePack();
            Message message = new Message();
            byte[] bytes = new byte[msg.readableBytes()];
            msg.readBytes(bytes);
            messagePack.read(bytes, message);
            System.out.println(message);
        }
    }
}
