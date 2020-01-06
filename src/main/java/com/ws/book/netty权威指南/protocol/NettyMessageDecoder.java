package com.ws.book.netty权威指南.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JunWu
 * 对字节解码
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public static final int LENGTH = 4;

    private ObjectDecoder objectDecoder;
    /**
     * 自定义名称,调试方便
     */
    private String name;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws Exception {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.objectDecoder = new ObjectDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionId(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int mapSize = frame.readByte();
        Map<String, Object> map = new HashMap<>(mapSize);

        for (int i = 0; i < mapSize; i++) {
            int keyLength = frame.readInt();
            byte[] bytes = new byte[keyLength];
            frame.readBytes(bytes);
            String key = new String(bytes, "UTF-8");
            map.put(key, objectDecoder.decode(frame));
        }
        header.setAttachment(map);
        message.setHeader(header);

        if (frame.readableBytes() > LENGTH) {
            message.setBody(objectDecoder.decode(frame));
        }
        return message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
