package com.ws.book.netty权威指南.protocol;

import lombok.Data;

/**
 * @author JunWu
 * netty协议栈 消息
 */
@Data
public class NettyMessage {
    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private Object body;


}
