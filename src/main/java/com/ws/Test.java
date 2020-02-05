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
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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


    /**
     * 可复用的类似定时的任务(只不过这边是定数执行任务), 当指定的参数1为变为0 时,触发一次 参数2指定的任务
     * 当参数1 达到0 时,会重置为原始数值
     * <p>
     * 值得注意的是: 当参数1为变成0是 , 之前的线程会被await,当变为0 时,执行指定任务,并唤醒之前的线程;
     *
     * @throws Exception
     */
    @org.junit.Test
    public void cyclicBarrier() throws Exception {

        // 指定10次,当10为变为0时,线程进行阻塞,当变为0时,执行一次任务,唤醒所有的被阻塞的线程
        CyclicBarrier barrier = new CyclicBarrier(10, () -> log.info("hello world!"));

        int count = 20;
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                try {
                    log.info("线程[{}]", Thread.currentThread().getName());
                    // 对10 进行--,当为0时执行任务并唤醒其余线程,同时重新设置为10
                    barrier.await();
                } catch (Exception e) {
                }
            }, i + "号").start();
        }
        LockSupport.park();
    }

    /**
     * 计数信号量
     * 定义锁的数量, 有多少数量就代表这有多少锁,
     * 当使用完毕需要将锁放回,以供其他线程使用
     * 不可重入 , 容易造成死锁
     * 当state <= 0 ,代表当前的信号灯已经没有,
     * 1.使用acquire则阻塞
     * 2.使用tryAcquire则尝试获取锁,返回boolean类型
     */
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

    /**
     * LockSupport.park
     * LockSupport.unPark(Thread)
     * CountdownLatch的功能类似于 Thread.join功能
     * 指定所需要完成的个数,当个数变为0 时,则唤醒执行了CountdownLatch.await()的线程
     *
     * @throws Exception
     */
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


    /**
     * 关于ReentrantLock 的公平锁与非公平锁:
     * 默认是使用非公平锁
     * 差异::
     * 非公平锁只是针对 当某个线程需要获取锁的时候(刚好这个锁被释放了,但是内部等待队列中有线程等待唤醒获取锁),
     * 这个时候该线程和等待的线程都有机会获取锁,也就是说非公平锁对进入等待队列阻塞的线程来说是不公平的;
     * 公平锁: 当线程需要获取某个锁时(刚好该锁被释放了,但是内部依旧含有等待线程获取锁),这个时候该线程无法获取锁,因为需要进行排队;
     * 这种公平锁就是建立在一个FIFO基础上
     *
     * @throws Exception
     */
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

    /**
     * JDK1.8 的时间
     *
     * @throws Exception
     */
    @org.junit.Test
    public void date() {

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

    /**
     * 读写锁
     * 场景:
     * 写 写 -> 互斥
     * 读 读 -> 共享
     * 读 写 -> 互斥
     */
    @org.junit.Test
    public void readWriteLock() throws Exception {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        new Thread(() -> {
            writeLock.lock();
            try {
                log.info("获取写锁");
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
            } finally {
                writeLock.unlock();
            }
        }, "写锁").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            readLock.lock();
            log.info("获取到读锁1");
            readLock.unlock();
        }, "读锁1").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            writeLock.lock();
            log.info("获取到写锁2");
            writeLock.unlock();
        }, "写2").start();

        TimeUnit.SECONDS.sleep(1);
        readLock.lock();
        log.info("获取到读锁2");
    }

    /**
     * 解决CAS的ABA问题
     * 原理 : 本质还是通过CAS + unsafe + volatile 实现的 ,
     * 通过unsafe直接替换volatile修饰的对象
     * <p>
     * Vector              ->      SynchronizedList ->                             CopyOnWriteArrayList
     * 所有操作都是同步            底层使用 Synchronized + ArrayList(装饰模式)     如名可知,只有写操作才是加锁的,使用ReentrantLock保证同步,直接通过Arrays拷贝新数据替换旧数据
     * 效率低                      效率与Vector感觉差不多                          乐观锁 + 读写锁,并发量高(个人觉得并不是真正的线程安全)
     * <p>
     * CopyOnWriteArraySet :
     * 与HashSet完全不全
     * 底层使用的是CopyOnWriteArrayList
     */
    @org.junit.Test
    public void aba() throws Exception {

        // 参数1: 代表需要进行同步修改的值
        // 参数2: 表示版本号
        AtomicStampedReference<Integer> reference = new AtomicStampedReference<>(100, 1);
        log.info("版本号为[{}] , 值为[{}]", reference.getStamp(), reference.getReference());

        Field pair = reference.getClass().getDeclaredField("pair");
        pair.setAccessible(true);
        log.info("pair[{}]", pair.get(reference));

        boolean b = reference.attemptStamp(100, 2);
        log.info("b[{}] , 版本号为[{}] , 值为[{}]", new Object[]{b, reference.getStamp(), reference.getReference()});

        reference.compareAndSet(100, 111, 2, 3);
        log.info("版本号为[{}] , 值为[{}]", reference.getStamp(), reference.getReference());
        log.info("pair[{}]", pair.get(reference));
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
