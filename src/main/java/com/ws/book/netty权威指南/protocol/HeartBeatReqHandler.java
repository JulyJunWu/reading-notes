package com.ws.book.netty权威指南.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author JunWu
 * 心跳请求Handler
 */
@Slf4j
public class HeartBeatReqHandler extends SimpleChannelInboundHandler<NettyMessage> {

    private volatile ScheduledFuture heartBeat;
    private volatile LocalDateTime lastTime;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {

        if (msg != null) {
            switch (MessageType.toMessageType(msg.getHeader().getType())) {
                case LOGIN_RES:
                    if (heartBeat != null) {
                        heartBeat.cancel(true);
                        heartBeat = null;
                    }
                    heartBeat = ctx.executor().scheduleAtFixedRate(new SendHeartBeat(ctx), 10L, 10L, TimeUnit.SECONDS);
                    break;
                case HEARTBEAT_RES:
                    LocalDateTime temp = lastTime;
                    lastTime = LocalDateTime.now();
                    log.info("心跳成功,时间[{}],上次时间[{}],消息体[{}]", new Object[]{lastTime, temp,msg.getBody()});
                    break;
                default:
                    ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 异常则关闭定时心跳请求任务
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }

    public static class SendHeartBeat implements Runnable {

        private final ChannelHandlerContext ctx;

        public SendHeartBeat(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            //构建心跳信息;
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.getType());
            message.setHeader(header);
            //发送心跳请求
            ctx.writeAndFlush(message);
        }
    }
}
