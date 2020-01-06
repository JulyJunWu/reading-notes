package com.ws.book.netty权威指南.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author JunWu
 * 自定义编码器
 */
public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    private ObjectEncoder objectEncoder;

    /**
     * 自定义名称,方便调试(可忽略)
     */
    private String name;

    public NettyMessageEncoder() throws Exception {
        this.objectEncoder = new ObjectEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("参数缺失!");
        }
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionId());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeByte(msg.getHeader().getAttachment().size());

        //设置附加信息,序列化value
        msg.getHeader().getAttachment().forEach((k, v) -> {
            try {
                byte[] bytes = k.getBytes("UTF-8");
                sendBuf.writeInt(bytes.length);
                sendBuf.writeBytes(bytes);
                objectEncoder.encode(v, sendBuf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (msg.getBody() != null) {
            objectEncoder.encode(msg.getBody(), sendBuf);
        } else {
            sendBuf.writeInt(0);
        }
        //设置数据的长度
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
