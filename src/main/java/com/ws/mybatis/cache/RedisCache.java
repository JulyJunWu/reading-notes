package com.ws.mybatis.cache;

import com.ws.mybatis.util.RedisUtils;
import org.apache.ibatis.cache.Cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author JunWu
 * 使用redis缓存实现二级缓存
 */
public class RedisCache implements Cache {

    private String id;

    private volatile int size = 0;

    public RedisCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object o, Object o1) {
        RedisUtils.putObject(id, o, o1);
        ++size;
    }

    @Override
    public Object getObject(Object o) {
        return RedisUtils.getObject(id, o);
    }

    @Override
    public Object removeObject(Object o) {
        --size;
        return RedisUtils.delete(id, o);
    }

    /**
     * 当执行insert|update等非查询操作时候清空该id为key的redis缓存
     */
    @Override
    public void clear() {
        RedisUtils.clear(id);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}
