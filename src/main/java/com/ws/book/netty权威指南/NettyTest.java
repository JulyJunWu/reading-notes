package com.ws.book.netty权威指南;

import com.ws.book.netty权威指南.messagepack.MessagePackClient;
import com.ws.book.netty权威指南.messagepack.MessagePackServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.net.InetSocketAddress;
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


    @Test
    public void testMd5() throws Exception {
        long start = System.currentTimeMillis();
        MessageDigest md = MessageDigest.getInstance("MD5");
        String s = "HelloWorld,Iloveyou";
        md.update(s.getBytes());
        byte[] digest = md.digest();
        String hexString = Hex.encodeHexString(digest);

        MessageDigest md1 = MessageDigest.getInstance("MD5");
        String s1 = "HelloWorld,Iloveou";
        md1.update(s1.getBytes());
        byte[] digest1 = md1.digest();
        String s2 = Hex.encodeHexString(digest1);

        System.out.println(hexString.equals(s2));
        System.out.println((System.currentTimeMillis() - start));

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

