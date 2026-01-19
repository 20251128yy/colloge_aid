package com.campus.delivery.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具类
 */
@Component
public class RedisUtil {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 存储数据到Redis
     */
//    public void set(String key, Object value, long time, TimeUnit unit) {
//        redisTemplate.opsForValue().set(key, value, time, unit);
//    }
//
//    /**
//     * 从Redis获取数据
//     */
//    public Object get(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    /**
//     * 删除Redis中的数据
//     */
//    public void delete(String key) {
//        redisTemplate.delete(key);
//    }
//
//    /**
//     * 设置过期时间
//     */
//    public boolean expire(String key, long time, TimeUnit unit) {
//        return redisTemplate.expire(key, time, unit);
//    }
//
//    /**
//     * 自增操作
//     */
//    public long increment(String key, long delta) {
//        return redisTemplate.opsForValue().increment(key, delta);
//    }
//
//    /**
//     * 自减操作
//     */
//    public long decrement(String key, long delta) {
//        return redisTemplate.opsForValue().decrement(key, delta);
//    }
}
