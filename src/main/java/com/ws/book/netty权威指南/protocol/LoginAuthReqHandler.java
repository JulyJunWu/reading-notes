package com.ws.book.netty权威指南.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JunWu
 * 客户端安全认证
 * ###职责:
 * #######1.连接建立,发送登录安全认证
 * #######2.接受服务器返回的信息,验证是否登录成功
 */
@Slf4j
public class LoginAuthReqHandler extends SimpleChannelInboundHandler<NettyMessage> {
    /**
     * 登录认证请求消息
     */
    private static NettyMessage reqLogMsg;

    static {
        reqLogMsg = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.getType());
        reqLogMsg.setHeader(header);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {

        if (msg.getHeader() != null && msg.getHeader().getType() == MessageType.LOGIN_RES.getType()) {
            byte loginResult = (byte) msg.getBody();
            // 捂手失败
            if (loginResult != (byte) 0) {
                log.error("客户端登录失败!");
                ctx.close();
            } else {
                log.info("客户端登录成功! [{}]", msg);
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 当连接建立成功后发送安全认证请求
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(reqLogMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }
}
