package com.ws.mybatis.util;

import redis.clients.jedis.Jedis;

/**
 * @author JunWu
 * redis工具
 */
public class RedisUtils {

    private static final Jedis jedis = new Jedis("localhost", 6379);

    private static final String PREFIX = "mybatis-cache:";

    /**
     * 保存数据到redis,使用的是redis string类型
     */
    public static void putObject(String key, Object field, Object value) {
        byte[] valueBytes = HessianUtils.serialize(value);
        byte[] fieldBytes = HessianUtils.serialize(field);
        // 将对象序列化
        jedis.hset((PREFIX + key).getBytes(), fieldBytes, valueBytes);
    }

    public static Object getObject(String key, Object field) {
        byte[] fieldBytes = HessianUtils.serialize(field);
        byte[] bytes = jedis.hget((PREFIX + key).getBytes(), fieldBytes);
        // 反序列化成对象
        return bytes == null ? null : HessianUtils.unSerialize(bytes);
    }

    public static boolean delete(String key, Object field) {
        return jedis.hdel((PREFIX + key).getBytes(), HessianUtils.serialize(field)) > 0;
    }

    public static boolean clear(String key) {
        return jedis.del((PREFIX + key).getBytes()) > 0;
    }
}
