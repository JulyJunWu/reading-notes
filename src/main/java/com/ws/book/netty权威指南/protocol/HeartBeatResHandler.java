package com.ws.book.netty权威指南.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author JunWu
 * 心跳响应处理
 */
@Slf4j
public class HeartBeatResHandler extends SimpleChannelInboundHandler<NettyMessage> {
    /**
     * 最后一次心跳成功时间
     */
    private LocalDateTime lastTime;

    /**
     * 用于存放所有NioSocketChannel,可以用于对所有Channel广播等操作!
     */
    private ChannelGroup channelGroup = new DefaultChannelGroup("存放客户端Channel", GlobalEventExecutor.INSTANCE);

    public static final AtomicBoolean GLOBAL_SCHEDULE = new AtomicBoolean();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
        if (msg == null) {
            return;
        }
        if (!GLOBAL_SCHEDULE.get()) {
            if (GLOBAL_SCHEDULE.compareAndSet(false, true)) {
                Runtime runtime = Runtime.getRuntime();
                // 开启一个定时任务用来计算内存使用率
                ctx.executor().scheduleWithFixedDelay(() -> {
                    long freeMemory = runtime.freeMemory();
                    long totalMemory = runtime.totalMemory();
                    long maxMemory = runtime.maxMemory();

                    //已使用多少字节
                    long usedByte = NettyServer.TOTAL_MEMORY - freeMemory;
                    long usedK = usedByte / 1024;
                    long usedM = usedK / 1024;
                    log.info("maxMemory[{}][{}],totalMemory[{}][{}],freeMemory[{}],已使用[{}B],已使用[{}K],已使用[{}M]",
                            new Object[]{NettyServer.MAX_MEMORY, maxMemory, NettyServer.TOTAL_MEMORY, totalMemory
                                    , freeMemory, usedByte, usedK, usedM});
                }, 1L, 10L, TimeUnit.SECONDS);
            }
        }

        /**
         * 只处理心跳请求
         */
        if (msg.getHeader().getType() == MessageType.HEARTBEAT_REQ.getType()) {
            String clientIp = ctx.channel().remoteAddress().toString();
            LocalDateTime temp = lastTime;
            lastTime = LocalDateTime.now();
            log.info("客户端[{}] 心跳请求成功,本次时间[{}],上次时间[{}]", new Object[]{clientIp, lastTime, temp});
            NettyMessage res = buildHeartBeatReq();
            ctx.writeAndFlush(res);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 构建心跳成功返回信息
     *
     * @return
     */
    private NettyMessage buildHeartBeatReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RES.getType());
        message.setHeader(header);
        return message;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 只会触发一次
        channelGroup.add(ctx.channel());
        log.info("当前group channel数量[{}]", channelGroup.size());
        super.channelActive(ctx);
    }

    /**
     * 这个方法重写的的前提是:
     * 用户代码不断的ctx.write(obj)但是却未flush,导致ChannelOutboundBuffer缓冲区不断扩大,最终超出缓冲区大小(默认最大是65535),
     * 如果ChannelHandler不重写该方法,那么是不会触发flush的,也就是不会向客户端写数据,数据一致积压在ChannelOutboundBuffer;
     * <p>
     * 当缓存达上限的时候,会触发channelWritabilityChanged函数,一般在此处直接flush将数据发送客户端;
     * <p>
     * 最好的办法就是尽量避免使用write,使用writeAndFlush代替
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        super.channelWritabilityChanged(ctx);
    }
}
