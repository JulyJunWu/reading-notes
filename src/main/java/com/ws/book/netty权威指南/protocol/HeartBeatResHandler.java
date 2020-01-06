package com.ws.book.netty权威指南.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {

        if (msg == null) {
            return;
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
}
