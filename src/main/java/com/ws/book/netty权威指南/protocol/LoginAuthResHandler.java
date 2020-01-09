package com.ws.book.netty权威指南.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author JunWu
 * 服务器安全认证处理器
 */
@Slf4j
public class LoginAuthResHandler extends SimpleChannelInboundHandler<NettyMessage> {

    public static final AtomicBoolean GLOBAL_SCHEDULE = new AtomicBoolean();

    /**
     * 用于存放所有NioSocketChannel,可以用于对所有Channel广播等操作!
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup("存放客户端Channel", GlobalEventExecutor.INSTANCE);

    /**
     * 白名单
     */
    private static final String[] WHITE_LIST = {"127.0.0.1", " 172.18.0.92"};

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
        if (msg.getHeader() != null && msg.getHeader().getType() == MessageType.LOGIN_REQ.getType()) {
            String ipString = ctx.channel().remoteAddress().toString();
            log.info("客户端[{}]请求登录,时间[{}]", ipString, LocalDateTime.now());
            NettyMessage resMsg;
            if (channelGroup.contains(ctx.channel())) {
                resMsg = buildResMessage((byte) -1);
                log.info("客户端[{}]登录失败,时间[{}]", ipString, LocalDateTime.now());
            } else {
                boolean isOk = false;
                InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                String hostAddress = inetSocketAddress.getAddress().getHostAddress();
                for (String ip : WHITE_LIST) {
                    if (ip.equals(hostAddress)) {
                        isOk = true;
                        break;
                    }
                }
                resMsg = isOk ? buildResMessage(new Byte((byte) 0)) : buildResMessage(new Byte((byte) -1));
                if (isOk) {
                    channelGroup.add(ctx.channel());
                    log.info("客户端[{}]验证成功,时间[{}]", ipString, LocalDateTime.now());
                }
            }
            ctx.writeAndFlush(resMsg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 构建安全认证成功信息
     *
     * @return
     */
    private NettyMessage buildResMessage(Object value) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RES.getType());
        message.setHeader(header);
        message.setBody(value);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String socketAddress = ctx.channel().remoteAddress().toString();
        ctx.fireExceptionCaught(cause);
        ctx.close();
        log.info("移除客户端[{}],剩余客户端数量[{}]", socketAddress, channelGroup.size());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.startScheduleTask(ctx);
        super.channelActive(ctx);
    }

    /**
     * 开启一个定时任务统计内存
     *
     * @param ctx
     */
    private void startScheduleTask(ChannelHandlerContext ctx) {
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
                    log.info("maxMemory[{}][{}],totalMemory[{}][{}],freeMemory[{}],已使用[{}B],已使用[{}K],已使用[{}M],全局连接数[{}]",
                            new Object[]{NettyServer.MAX_MEMORY, maxMemory, NettyServer.TOTAL_MEMORY, totalMemory
                                    , freeMemory, usedByte, usedK, usedM, channelGroup.size()});
                }, 1L, 60L, TimeUnit.SECONDS);
            }
        }
    }
}
