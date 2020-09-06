package com.lyl.utils;

import cn.hutool.setting.dialect.Props;
import com.alibaba.fastjson.JSON;
import com.lyl.constant.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @Title: RedisTemplateService
 * @Description: 工具类
 * @date: 2019年9月5日 下午7:01:42
 */
@Service("redisTemplateUtils")
public class RedisTemplateUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private static boolean flag;

    static {
        // 是否启用redis, 注意：默认是不启用的
        flag = new Props("application-redis.properties").getBool(SystemConstant.DEFAULT_IF_DISABLE_REDIS);
    }


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * @param key
     * @param value
     * @return
     * @Description: 插入
     * @date: 2019年9月5日 下午7:02:45
     */
    public <T> boolean set(String key, T value) {
        // 是否启用redis
        if (flag) {
            return true;
        }

        try {

            // 任意类型转换成String
            String val = beanToString(value);

            if (val == null || val.length() <= 0) {
                return false;
            }

            stringRedisTemplate.opsForValue().set(key, val);
            return true;
        } catch (Exception e) {
            logger.error("redis set fail .", e);
            return false;
        }
    }

    /**
     * @param key
     * @param clazz
     * @return
     * @Description: 根据key获取元素
     * @date: 2019年9月5日 下午7:03:16
     */
    public <T> T get(String key, Class<T> clazz) {

        // 是否启用redis
        if (flag) {
            return stringToBean(null, clazz);
        }

        try {
            String value = stringRedisTemplate.opsForValue().get(key);

            return stringToBean(value, clazz);
        } catch (Exception e) {
            logger.error("redis get fail .", e);
            return null;
        }
    }


    /**
     * @param prex
     * @Description: 模糊查询key，然后删除模糊查询出的所有key
     * @date: 2019年9月5日 下午7:53:25
     */
    public void deleteByPrex(String prex) {
        if (prex != null && !"".equals(prex)) {
            Set<String> keys = stringRedisTemplate.keys(prex);
            if (keys.size() > 0) {
                stringRedisTemplate.delete(keys);
            }
        }
    }


    /**
     * 根据　key　删除redis缓存
     *
     * @param key
     */
    public void deleteByKey(String key) {
        // 是否启用redis
        if (flag) {
            return;
        }

        if (key != null && !"".equals(key)) {
            stringRedisTemplate.delete(key);
        }
    }


    private static final Long SUCCESS = 1L;

    private final String releaseDistributedLocakLua = "if redis.call('get', KEYS[1]) == ARGV[1] " +
            "then " +
            "return redis.call('del', KEYS[1]) " +
            "else " +
            "return 0 " +
            "end";

    private final String getDistributedLocakLua = "if redis.call('setNx',KEYS[1],ARGV[1])  then " +
            "   if redis.call('get',KEYS[1])==ARGV[1] then " +
            "      return redis.call('expire',KEYS[1],ARGV[2]) " +
            "   else " +
            "      return 0 " +
            "   end " +
            "end";


    /**
     * 获取分布式锁
     *
     * @param lockKey
     * @param lockValue
     * @param expireTime
     * @return
     */
    public boolean tryGetDistributedLock(final String lockKey, final String lockValue, int expireTime) {
        // 是否启用redis
        if (flag) {
            return true;
        }

        // redis脚本，执行脚本的返回类型为 Long
        RedisScript<Long> redisScript = new DefaultRedisScript<>(getDistributedLocakLua, Long.class);

        // 对非string类型的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey),
                lockValue, String.valueOf(expireTime));

        if (SUCCESS.equals(result)) {
            return true;
        }

        return false;
    }


    /**
     * 释放分布式锁
     *
     * @param lockKey
     * @param lockValue
     * @return
     */
    public synchronized boolean releaseDistributedLock(final String lockKey, final String lockValue) {
        // 是否启用redis
        if (flag) {
            return true;
        }

        // redis脚本，执行脚本的返回类型为 Long
        RedisScript<Long> redisScript = new DefaultRedisScript<>(releaseDistributedLocakLua, Long.class);

        // 对非string类型的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        try {
            Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), lockValue);
            if (SUCCESS.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("releaseDistributedLock fail : ", e);
        }
        return false;
    }


    /**
     * @param value
     * @param clazz
     * @return
     * @Description: 将字符串转为对象
     * @date: 2019年9月5日 下午7:04:05
     */
    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String value, Class<T> clazz) {
        if (value == null || value.length() <= 0 || clazz == null) {
            return null;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else if (clazz == List.class) {
            return JSON.toJavaObject(JSON.parseArray(value), clazz);
        } else {
            return JSON.toJavaObject(JSON.parseObject(value), clazz);
        }
    }


    /**
     * @param value
     * @return
     * @Description: 将任意对象转为字符串
     * @date: 2019年9月5日 下午7:04:41
     */
    private <T> String beanToString(T value) {

        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

}
