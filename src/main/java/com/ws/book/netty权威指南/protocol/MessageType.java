package com.ws.book.netty权威指南.protocol;

/**
 * @author JunWu
 * 消息类型枚举
 */
public enum MessageType {

    /**
     * 业务请求
     */
    BUSINESS_REQ((byte) 0),
    /**
     * 业务响应
     */
    BUSINESS_RES((byte) 1),
    /**
     * 既是业务请求又是响应
     */
    BUSINESS_REQ_RES((byte) 2),
    /**
     * 捂手请求信息(登录请求)
     */
    LOGIN_REQ((byte) 3),
    /**
     * 捂手应答信息(登录响应)
     */
    LOGIN_RES((byte) 4),
    /**
     * 心跳请求
     */
    HEARTBEAT_REQ((byte) 5),

    /**
     * 心跳应答
     */
    HEARTBEAT_RES((byte) 6);

    private byte type;

    MessageType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public static MessageType toMessageType(byte value) {
        for (MessageType type : values()) {
            if (type.getType() == value) {
                return type;
            }
        }
        return null;
    }
}
