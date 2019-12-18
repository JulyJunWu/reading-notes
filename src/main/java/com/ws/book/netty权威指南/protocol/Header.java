package com.ws.book.netty权威指南.protocol;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JunWu
 * 消息头
 */
@Data
public final class Header {

    private int crcCode = 0xabef0101;
    /**
     * 消息长度
     */
    private int length;
    /**
     * 回话ID
     */
    private long sessionId;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 消息优先级
     */
    private byte priority;
    /**
     * 附件,为了后续的扩展
     */
    private Map<String, Object> attachment = new HashMap(8);

}
