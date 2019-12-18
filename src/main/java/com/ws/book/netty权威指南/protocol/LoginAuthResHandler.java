package com.ws.book.netty权威指南.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JunWu
 * 服务器安全认证处理器
 */
@Slf4j
public class LoginAuthResHandler extends SimpleChannelInboundHandler<NettyMessage> {
    /**
     * 存放已认证通过的客户端连接
     */
    private static Map<String, Boolean> ALREADY_CONNECTION = new ConcurrentHashMap<>();

    /**
     * 白名单
     */
    private static final String[] WHITE_LIST = {"127.0.0.1", " 172.18.0.92"};

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
        log.info("当前客户端数量->{}", ALREADY_CONNECTION.size());
        if (msg.getHeader() != null && msg.getHeader().getType() == MessageType.LOGIN_REQ.getType()) {
            String ipString = ctx.channel().remoteAddress().toString();
            log.info("客户端[{}]请求登录,时间->{}", ipString, LocalDateTime.now());
            NettyMessage resMsg;
            if (ALREADY_CONNECTION.containsKey(ipString)) {
                resMsg = buildResMessage((byte) -1);
                log.info("客户端[{}]登录失败,时间->{}", ipString, LocalDateTime.now());
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
                    ALREADY_CONNECTION.put(ipString, Boolean.TRUE);
                    log.info("客户端[{}]验证成功,时间->{}", ipString, LocalDateTime.now());
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
        ALREADY_CONNECTION.remove(socketAddress);
        ctx.fireExceptionCaught(cause);
        ctx.close();
        log.info("移除客户端[{}],剩余客户端数量[{}]", socketAddress, ALREADY_CONNECTION.size());
    }
}
