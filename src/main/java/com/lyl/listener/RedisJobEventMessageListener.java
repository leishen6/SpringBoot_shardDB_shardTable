package com.lyl.listener;

import com.lyl.bean.JobBean;
import com.lyl.constant.SystemConstant;
import org.quartz.Job;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @PACKAGE_NAME: com.lyl.listener
 * @ClassName: RedisJobEventMessageListener
 * @Description: 重写了 Redis 的 Key 失效监听器，使用其实现 定时任务 ;
 * 当redis 中的key过期时，触发一个事件，并不会准点触发事件，适用于时间不是特别敏感的触发需求 ;
 * 我们可以算好需要执行的时间间隔作为key失效时间，这样就可以保证到点执行逻辑了 ;
 * @Date: 2020-12-21 11:20
 * @Author: 木子雷 公众号
 **/
@Component
public class RedisJobEventMessageListener extends KeyExpirationEventMessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StringRedisTemplate redisTemplate;


    public RedisJobEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    /**
     * redis 过期消息处理器
     *
     * @param message
     */
    @Override
    protected void doHandleMessage(Message message) {
        // 过期消息的key
        String key = message.toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 这个就是过期的key ，过期后，也就是事件触发后对应的value是拿不到的。
        // 这里实现业务逻辑，如果是服务器集群的话需要使用分布式锁进行抢占执行。
        logger.info("key = {}", key);
        logger.info("end = {}", dateFormat.format(new Date()));

        if (SystemConstant.map.containsKey(key)) {
            JobBean redisJobBean = SystemConstant.map.get(key);
            try {
                // 注意：这里是你自己的定时任务处理的内容
                Job redisJob = (Job) Class.forName(redisJobBean.getJobClass()).newInstance();
                redisJob.execute(null);

            } catch (InstantiationException e) {
                logger.error("redis 定时任务：{} 过期处理失败！", key, e);
            } catch (IllegalAccessException e) {
                logger.error("redis 定时任务：{} 过期处理失败！", key, e);
            } catch (ClassNotFoundException e) {
                logger.error("redis 定时任务：{} 过期处理失败！", key, e);
            } catch (JobExecutionException e) {
                logger.error("redis 定时任务：{} 过期处理失败！", key, e);
            }

            // redis key过期处理完后，需要重新设置key及其过期时间，实现定时任务的定时执行
            redisTemplate.boundValueOps(redisJobBean.getJobGroup()).set(redisJobBean.getJobGroup(),
                    Integer.parseInt(redisJobBean.getCronExpression()), TimeUnit.SECONDS);
        }
    }

}
