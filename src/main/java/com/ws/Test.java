package com.ws;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * @Description:
 * @Author: QiuJunWu
 * @Date: 2019/10/11 0011 13:02
 */
@Slf4j
public class Test implements Serializable {

    public static final AtomicInteger COUNT = new AtomicInteger();
    public static final int NUM = 100;

    @org.junit.Test
    public void testSemaphore() {
        Semaphore semaphore = new Semaphore(1);
        for (int i = 0; i < NUM; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        // 获取锁 -1
                        boolean acquire = semaphore.tryAcquire();
                        if (acquire) {
                            int num = COUNT.incrementAndGet();
                            log.info("线程[{}] 获取到锁, 顺序[{}]", Thread.currentThread().getName(), num);
                            TimeUnit.SECONDS.sleep(10);
                            // 释放锁, +1
                            semaphore.release();
                        } else {
                            log.info("线程[{}] 未获取到锁", Thread.currentThread().getName());
                            TimeUnit.SECONDS.sleep(5);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        LockSupport.park();
    }

    @org.junit.Test
    public void testCountdownLatch() throws Exception {
        CountDownLatch downLatch = new CountDownLatch(2);
        IntStream.range(0, 2).forEach(p -> new Thread(() -> {
            while (true) {
                System.out.println(Thread.currentThread().getName());
                // 将CAS 将 state 减一
                downLatch.countDown();
                break;
            }
        }).start());

        new Thread(() -> {
            while (true) {
                try {
                    // 等待state小于等于0 , 唤醒
                    downLatch.await();
                    System.out.println(1);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 当state大于0的时候,底层使用LockSupport.park()阻塞该线程;
        // 当state小于等于0后,直接执行或者通过LockSupport.unPark()唤醒;
        downLatch.await();
        System.out.println(Thread.currentThread().getName());
    }


    @org.junit.Test
    public void testLock() throws Exception {
        ReentrantLock lock = new ReentrantLock();

        Thread thread = new Thread(() -> {
            boolean ok = true;
            while (ok) {
                // 通过CAS获取值,成功则获取锁,失败则通过 LockSupport.park()进入阻塞
                lock.lock();
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 释放锁,并通过 LockSupport.unPark(Thread)唤醒被阻塞的线程;
                    lock.unlock();
                    ok = false;
                }
            }
        });

        thread.start();
        TimeUnit.SECONDS.sleep(1);
        lock.lock();
        System.out.println(2);

    }

    @org.junit.Test
    public void date() throws Exception {

        LocalDateTime now = LocalDateTime.now();

        String format = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(format);
        format = now.format(ISO_LOCAL_DATE);
        System.out.println(format);
        format = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(format);

        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendLiteral(' ')
                .append(DateTimeFormatter.ISO_LOCAL_TIME).toFormatter();

        format = now.format(dateTimeFormatter);
        System.out.println(format);
    }


    @org.junit.Test
    public void server() throws Exception {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = bootstrap.group(boss, work).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new DecorderHandler());
                        pipeline.addLast(new StringHandler());
                    }
                }).bind(8888);

        channelFuture.channel().closeFuture().sync();
    }

    @org.junit.Test
    public void client() throws Exception {

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap.group(group).channel(NioSocketChannel.class).handler(new EncoderHandler()).connect("127.0.0.1", 8888);
        connect.channel().closeFuture().sync();
    }
}

class StringHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
    }
}

class DecorderHandler extends ByteToMessageDecoder {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        list.add(new String(bytes));
    }
}

class EncoderHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int i = 0; i < 100; i++) {
            String msg = "现在时间是:" + LocalDateTime.now();
            byte[] bytes = msg.getBytes();
            ByteBuf buffer = ctx.alloc().buffer(4 + bytes.length);
            buffer.writeInt(bytes.length);
            buffer.writeBytes(bytes);
            ctx.writeAndFlush(buffer);
        }
    }

}
