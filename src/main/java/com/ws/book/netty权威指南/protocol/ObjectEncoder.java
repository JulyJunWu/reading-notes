package com.ws.book.netty权威指南.protocol;

import com.ws.book.netty权威指南.marshalling.MarshallingFactory;
import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

/**
 * @author JunWu
 * 对象编码/序列化
 */
public class ObjectEncoder {

    private static final byte[] LENGTH_PALACE_HOLDER = new byte[4];
    private Marshaller marshaller;

    public ObjectEncoder() throws Exception {
        this.marshaller = MarshallingFactory.createMarshaller();
    }

    public void encode(Object msg, ByteBuf out) throws IOException {
        try {
            //写索引
            int writerIndex = out.writerIndex();
            out.writeBytes(LENGTH_PALACE_HOLDER);

            ChannelBufferByteOutput channelBufferByteOutput = new ChannelBufferByteOutput(out);
            marshaller.start(channelBufferByteOutput);
            marshaller.writeObject(msg);
            marshaller.finish();
            //设置msg消息长度
            out.setInt(writerIndex, out.writerIndex() - writerIndex - 4);
        } finally {
            marshaller.close();
        }
    }

}

class ChannelBufferByteOutput implements ByteOutput {

    private final ByteBuf buffer;

    /**
     * Create a new instance which use the given {@link ByteBuf}
     */
    public ChannelBufferByteOutput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    @Override
    public void close() throws IOException {
        // Nothing to do
    }

    @Override
    public void flush() throws IOException {
        // nothing to do
    }

    @Override
    public void write(int b) throws IOException {
        buffer.writeByte(b);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        buffer.writeBytes(bytes);
    }

    @Override
    public void write(byte[] bytes, int srcIndex, int length) throws IOException {
        buffer.writeBytes(bytes, srcIndex, length);
    }

    /**
     * Return the {@link ByteBuf} which contains the written content
     */
    ByteBuf getBuffer() {
        return buffer;
    }
}
