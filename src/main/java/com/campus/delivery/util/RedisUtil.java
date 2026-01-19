//package com.campus.delivery.util;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * Redis缓存工具类
// * 修复点：
// * 1. 取消核心代码注释，还原工具类功能
// * 2. 增加空值判断，提升方法健壮性
// * 3. 保留@Component注解，确保Spring能扫描到该类
// */
//@Component // 必须保留，Spring才能将该类纳入容器管理
//public class RedisUtil {
//
//    // 注入RedisTemplate，取消注释（核心依赖）
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    /**
//     * 存储数据到Redis
//     * @param key 键
//     * @param value 值
//     * @param time 过期时间
//     * @param unit 时间单位
//     */
//    public void set(String key, Object value, long time, TimeUnit unit) {
//        if (key == null || key.isEmpty() || value == null) {
//            throw new IllegalArgumentException("key和value不能为空");
//        }
//        redisTemplate.opsForValue().set(key, value, time, unit);
//    }
//
//    /**
//     * 从Redis获取数据
//     * @param key 键
//     * @return 对应的值（null表示不存在）
//     */
//    public Object get(String key) {
//        if (key == null || key.isEmpty()) {
//            return null;
//        }
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    /**
//     * 删除Redis中的数据
//     * @param key 键
//     */
//    public void delete(String key) {
//        if (key != null && !key.isEmpty()) {
//            redisTemplate.delete(key);
//        }
//    }
//
//    /**
//     * 设置过期时间
//     * @param key 键
//     * @param time 过期时间
//     * @param unit 时间单位
//     * @return true-设置成功，false-设置失败
//     */
//    public boolean expire(String key, long time, TimeUnit unit) {
//        if (key == null || key.isEmpty() || time <= 0) {
//            return false;
//        }
//        return Boolean.TRUE.equals(redisTemplate.expire(key, time, unit));
//    }
//
//    /**
//     * 自增操作
//     * @param key 键
//     * @param delta 增量（正数）
//     * @return 自增后的值
//     */
//    public long increment(String key, long delta) {
//        if (key == null || key.isEmpty() || delta <= 0) {
//            throw new IllegalArgumentException("key不能为空，delta必须为正数");
//        }
//        return redisTemplate.opsForValue().increment(key, delta);
//    }
//
//    /**
//     * 自减操作
//     * @param key 键
//     * @param delta 减量（正数）
//     * @return 自减后的值
//     */
//    public long decrement(String key, long delta) {
//        if (key == null || key.isEmpty() || delta <= 0) {
//            throw new IllegalArgumentException("key不能为空，delta必须为正数");
//        }
//        return redisTemplate.opsForValue().decrement(key, delta);
//    }
//}
package com.campus.delivery.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RedisUtil {

    @Autowired(required = false)  // 允许为null
    private RedisTemplate<String, Object> redisTemplate;

    // 如果Redis不可用，使用内存缓存
    private static final ConcurrentHashMap<String, CacheItem> memoryCache = new ConcurrentHashMap<>();

    private static class CacheItem {
        Object value;
        long expireTime;

        CacheItem(Object value, long expireSeconds) {
            this.value = value;
            this.expireTime = System.currentTimeMillis() + expireSeconds * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    public void set(String key, Object value, long time) {
        if (redisTemplate != null) {
            try {
                // 尝试使用Redis
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                System.out.println("✅ 使用Redis存储: " + key);
            } catch (Exception e) {
                // Redis不可用，使用内存缓存
                System.out.println("⚠️ Redis不可用，使用内存缓存: " + key);
                memoryCache.put(key, new CacheItem(value, time));
            }
        } else {
            // RedisTemplate为null，使用内存缓存
            System.out.println("⚠️ Redis未配置，使用内存缓存: " + key);
            memoryCache.put(key, new CacheItem(value, time));
        }
    }

    public Object get(String key) {
        if (redisTemplate != null) {
            try {
                Object value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    System.out.println("✅ 从Redis获取: " + key);
                }
                return value;
            } catch (Exception e) {
                // Redis异常，尝试从内存获取
                System.out.println("⚠️ Redis异常，尝试从内存获取: " + key);
            }
        }

        // 从内存缓存获取
        CacheItem item = memoryCache.get(key);
        if (item != null) {
            if (item.isExpired()) {
                memoryCache.remove(key);
                return null;
            }
            System.out.println("⚠️ 从内存缓存获取: " + key);
            return item.value;
        }
        return null;
    }

    public Boolean delete(String key) {
        if (redisTemplate != null) {
            try {
                Boolean result = redisTemplate.delete(key);
                System.out.println("✅ 从Redis删除: " + key);
                return result;
            } catch (Exception e) {
                System.out.println("⚠️ Redis删除异常: " + key);
            }
        }

        // 从内存缓存删除
        System.out.println("⚠️ 从内存缓存删除: " + key);
        return memoryCache.remove(key) != null;
    }

    public Boolean hasKey(String key) {
        if (redisTemplate != null) {
            try {
                return redisTemplate.hasKey(key);
            } catch (Exception e) {
                // Redis异常
            }
        }

        // 检查内存缓存
        CacheItem item = memoryCache.get(key);
        return item != null && !item.isExpired();
    }
}