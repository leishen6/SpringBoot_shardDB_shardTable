package com.lyl.utils;

import cn.hutool.setting.dialect.Props;
import com.alibaba.fastjson.JSON;
import com.lyl.constant.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Title: RedisTemplateService
 * @Description:  工具类
 * @date: 2019年9月5日 下午7:01:42
 */
@Service
public class RedisTemplateUtils {

	private static boolean flag;

	static{
		// 是否启用redis, 注意：默认是不启用的
		flag = new Props("application-redis.properties").getBool(SystemConstant.DEFAULT_IF_DISABLE_REDIS);
	}


	@Autowired
	StringRedisTemplate stringRedisTemplate ;


	/**
	 * @Description: 插入
	 * @param key
	 * @param value
	 * @return
	 *@date: 2019年9月5日 下午7:02:45
	 */
	public <T> boolean set(String key, T value) {
		// 是否启用redis
		if (flag){
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
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @Description: 根据key获取元素
	 * @param key
	 * @param clazz
	 * @return
	 *@date: 2019年9月5日 下午7:03:16
	 */
	public <T> T get(String key,Class<T> clazz){

		// 是否启用redis
		if (flag){
			return stringToBean(null, clazz);
		}

        try {
            String value = stringRedisTemplate.opsForValue().get(key);

            return stringToBean(value,clazz);
        }catch (Exception e){
        	e.printStackTrace();
            return null ;
        }
    }
	
	
	/**
	 * @Description: 模糊查询key，然后删除模糊查询出的所有key
	 * @param prex
	 *@date: 2019年9月5日 下午7:53:25
	 */
	public void deleteByPrex(String prex) {
        if (prex != null && !"".equals(prex)){
			Set<String> keys = stringRedisTemplate.keys(prex);
			if (keys.size() > 0) {
				stringRedisTemplate.delete(keys);
			}
		}
    }


	/**
	 *  根据　key　删除redis缓存
	 * @param key
	 */
	public void deleteByKey(String key) {
		// 是否启用redis
		if (flag){
			return ;
		}

		if (key != null && !"".equals(key)){
			stringRedisTemplate.delete(key);
		}
	}
	
	
	
	/**
	 * @Description: 将字符串转为对象
	 * @param value
	 * @param clazz
	 * @return
	 *@date: 2019年9月5日 下午7:04:05
	 */
	@SuppressWarnings("unchecked")
    private <T> T stringToBean(String value, Class<T> clazz) {
        if(value==null||value.length()<=0||clazz==null){
            return null;
        }

        if(clazz ==int.class ||clazz==Integer.class){
            return (T)Integer.valueOf(value);
        }
        else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(value);
        }
        else if(clazz==String.class){
            return (T)value;
        }else if (clazz == List.class){
            return JSON.toJavaObject(JSON.parseArray(value),clazz);
        }else {
			return JSON.toJavaObject(JSON.parseObject(value),clazz);
		}
    }
	
	
	/**
	 * @Description: 将任意对象转为字符串
	 * @param value
	 * @return
	 *@date: 2019年9月5日 下午7:04:41
	 */
	private <T> String beanToString(T value) {

        if(value==null){
            return null;
        }
        Class <?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }
        else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }
        else if(clazz==String.class){
            return (String)value;
        }else {
            return JSON.toJSONString(value);
        }
    }

}
