package com.ws.book.netty权威指南;

import com.ws.book.netty权威指南.messagepack.MessagePackClient;
import com.ws.book.netty权威指南.messagepack.MessagePackServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

/**
 * @author JunWu
 * netty测试
 */
public class NettyTest {


    /**
     * 测试使用
     * LengthFieldBasedFrameDecoder  用于对进栈数据解码
     * LengthFieldPrepender          用于对出栈数据进行编码
     * <p>
     * 用于自定义消息长度 + 数据
     */
    @Test
    public void testLength() throws Exception {
        Class<? extends ChannelHandler>[] aClass = new Class[]{LengthFieldBasedFrameDecoder.class, MessagePackServer.MsgPackDecoder2.class};
        Object[] args = new Object[]{null, new Object[]{65535, 0, 2, 0, 2}, null, null, null};
        Object[] argsType = new Object[]{null, new Class[]{int.class, int.class, int.class, int.class, int.class}, null, null, null};

        new Thread(() -> NettyUtils.startNettyServer(6666, aClass, args, argsType)).start();

        TimeUnit.SECONDS.sleep(2);
        NettyUtils.startNettyClient(new InetSocketAddress(6666), new LengthFieldPrepender(2), new MessagePackClient.SendMessageHandler2());
    }

    @Test
    public void testEvent() throws Exception {

        NioEventLoopGroup executors = new NioEventLoopGroup(2);

        int count = executors.executorCount();
        System.out.println(count);

        for (int i = 0; i < 10; i++) {
            executors.execute(new Task());
        }
        TimeUnit.SECONDS.sleep(100000);
    }

    /**
     * 获取JDK Unsafe对象
     *
     * @throws Exception
     */

    class Teacher {
        private volatile long age;

        public long getAge() {
            return age;
        }

        public void setAge(long age) {
            this.age = age;
        }
    }

    @Test
    public void unSafe() throws Exception {

        Teacher teacher = new Teacher();
        teacher.setAge(20);
        //此方法调用只能是根加载器加载的类调用,否则报错
        //Unsafe unsafe = Unsafe.getUnsafe();
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        System.out.println(unsafe);
        unsafe.putIntVolatile(teacher, teacher.age, 10);
    }

    @Test
    public void byteBuf() {
        ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
        ByteBuf buffer = allocator.buffer(13);
        buffer.writeInt(100);
        buffer.writeInt(101);
        buffer.writeInt(102);
        buffer.writeByte('\n');
        //  int i = buffer.forEachByte(ByteProcessor.FIND_LF);
        //  获取NIO的ByteBuffer
        ByteBuffer byteBuffer = buffer.nioBuffer();
        buffer.readInt();
        buffer.readInt();
        // 丢弃已读的数据,释放已读的数据所占用的内存
        buffer.discardReadBytes();
        // 引用计数+1
        buffer.retain();
        // 释放-1
        buffer.release();

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(buffer.readableBytes() - 1);
        buffer.readBytes(buf);


        ByteBuffer allocate = ByteBuffer.allocate(1024);
        allocate.put("netty".getBytes());
        allocate.flip();

        byte[] bytes = new byte[allocate.remaining()];
        allocate.get(bytes);
        allocate.clear();

        System.out.println(new String(bytes));
    }


    @Test
    public void testMd5() throws Exception {
        long start = System.currentTimeMillis();
        MessageDigest md = MessageDigest.getInstance("MD5");
        String s = "HelloWorld";
        md.update(s.getBytes());
        byte[] digest = md.digest();
        String hexString = Hex.encodeHexString(digest);

        MessageDigest md1 = MessageDigest.getInstance("MD5");
        String s1 = "HelloWorld";
        md1.update(s1.getBytes());
        byte[] digest1 = md1.digest();
        String s2 = Hex.encodeHexString(digest1);

        System.out.println(hexString.equals(s2));
        System.out.println((System.currentTimeMillis() - start));

    }

    @Test
    public void byteBufUtil() {
        //转换成16进制
        String dump = ByteBufUtil.hexDump("Hello World".getBytes());
        System.out.println(dump);

        //解码成字节
        byte[] bytes = ByteBufUtil.decodeHexDump(dump);
        String s = new String(bytes);
        System.out.println(s);

        //  将字符串包装成StringCharBuffer
        CharBuffer charBuffer = CharBuffer.wrap("Hello World");
        //  分配缓冲
        ByteBuf byteBuf = ByteBufUtil.encodeString(ByteBufAllocator.DEFAULT, charBuffer, Charset.defaultCharset());
        //  解码
        String string = byteBuf.toString(Charset.defaultCharset());
        System.out.println(string);
    }
}

@Slf4j
class Task implements Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                log.info("name[{}],toString[{}]", Thread.currentThread().getName(), Thread.currentThread());
                TimeUnit.SECONDS.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

