package com.ws.framework.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @Description:
 * @Date: 2019/8/13 0013 14:36
 */
public class RedisDemo {

    private static Jedis jedis;

    @Before
    public void before() {
        jedis = new Jedis("server-1", 6379);
    }

    @Test
    public void create() {

        //设置kay-value
        jedis.set("name", "ZWS");

        //设置过期时间
        jedis.setex("expire", 5, "nice");

        //一次性设置多个值,必须是2的倍数, 一一对应 , 设置的时候是 good->happy ; hard->six
        jedis.mset("good", "happy", "hard", "six");

        //给指定的key的value追加内容
        jedis.append("good", ";不积跬步无以至千里");

        //获取
        String good = jedis.get("good");

        //一次性获取多个key值
        List<String> mget = jedis.mget("good", "hard");

        //map结构
        jedis.hset("user", "百度", "www.baidu.com");
        jedis.hset("user", "淘宝", "www.taobao.com");
    }

}
