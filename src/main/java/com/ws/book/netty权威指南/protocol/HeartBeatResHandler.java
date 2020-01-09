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
        assert msg != null;
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
