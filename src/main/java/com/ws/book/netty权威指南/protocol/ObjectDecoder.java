package com.ws.book.netty权威指南.protocol;

import com.ws.book.netty权威指南.marshalling.MarshallingFactory;
import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * @author JunWu
 * 对字节进行反序列化成对象
 */
public class ObjectDecoder {

    private Unmarshaller unmarshaller;

    public ObjectDecoder() throws Exception {
        this.unmarshaller = MarshallingFactory.createUnmarshaller();
    }

    /**
     * 对ByteBuf进行解码
     *
     * @param in
     * @return
     */
    public Object decode(ByteBuf in) throws Exception {
        int length = in.readInt();
        ByteBuf slice = in.slice(in.readerIndex(), length);
        try {
            ChannelBufferByteInput byteInput = new ChannelBufferByteInput(slice);
            unmarshaller.start(byteInput);
            Object object = unmarshaller.readObject();
            unmarshaller.finish();
            // 设置已读索引位置
            in.readerIndex(in.readerIndex() + length);
            return object;
        } finally {
            unmarshaller.close();
        }
    }
}

class ChannelBufferByteInput implements ByteInput {

    private final ByteBuf buffer;

    public ChannelBufferByteInput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    @Override
    public void close() throws IOException {
        // nothing to do
    }

    @Override
    public int available() throws IOException {
        return buffer.readableBytes();
    }

    @Override
    public int read() throws IOException {
        if (buffer.isReadable()) {
            return buffer.readByte() & 0xff;
        }
        return -1;
    }

    @Override
    public int read(byte[] array) throws IOException {
        return read(array, 0, array.length);
    }

    @Override
    public int read(byte[] dst, int dstIndex, int length) throws IOException {
        int available = available();
        if (available == 0) {
            return -1;
        }

        length = Math.min(available, length);
        buffer.readBytes(dst, dstIndex, length);
        return length;
    }

    @Override
    public long skip(long bytes) throws IOException {
        int readable = buffer.readableBytes();
        if (readable < bytes) {
            bytes = readable;
        }
        buffer.readerIndex((int) (buffer.readerIndex() + bytes));
        return bytes;
    }

}