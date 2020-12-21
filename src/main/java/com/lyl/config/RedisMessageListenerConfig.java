package com.lyl.config;

import cn.hutool.setting.dialect.Props;
import com.lyl.bean.JobBean;
import com.lyl.constant.SystemConstant;
import com.lyl.listener.RedisJobEventMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @PACKAGE_NAME: com.lyl.config
 * @ClassName: MessageListenerConfig
 * @Description: redis 消息监听器配置类，使用注解将自定义的消息过期监听器注入到IOC中等
 * @Date: 2020-12-21 11:22
 * @Author: 木子雷 公众号
 **/
@Configuration
public class RedisMessageListenerConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StringRedisTemplate redisTemplate;


    /**
     * redis 定时任务启动
     */
    public void startRedisJob()
            throws Exception {
        List<JobBean> redisJobBeanList = getRedisJobBeanByConfig();
        if (redisJobBeanList != null && redisJobBeanList.size() > 0) {
            for (JobBean redisJobBean : redisJobBeanList) {
                // 调用 redisTemplate 对象设置一个300s 后过期的键，不出意外 300s 后键过期后会触发事件打印结果
                redisTemplate.boundValueOps(redisJobBean.getJobGroup()).set(redisJobBean.getJobGroup(),
                        Integer.parseInt(redisJobBean.getCronExpression()), TimeUnit.SECONDS);
            }
        }
    }


    /**
     * 获取 redis 定时任务配置参数
     *
     * @return
     * @throws Exception
     */
    private List<JobBean> getRedisJobBeanByConfig()
            throws Exception {
        Props props = new Props("application-redis.properties");
        String redisJobList = props.getStr("redisJobList");
        if (redisJobList == null || "".equals(redisJobList)) {
            throw new Exception("Redis 定时任务集合 redisJobList 未配置 . . . . . .");
        }
        List<JobBean> redisJobBeanList = new ArrayList<>();
        String[] redisJobs = redisJobList.split(",");

        for (int i = 0; i < redisJobs.length; i++) {
            JobBean jobBean = new JobBean();
            jobBean.setJobClass(props.getStr(redisJobs[i] + ".jobClass"));
            jobBean.setJobName(props.getStr(redisJobs[i] + ".name"));
            // 用于设置redis 的key
            jobBean.setJobGroup(redisJobs[i]);
            jobBean.setCronExpression(props.getStr(redisJobs[i] + ".expireTime"));
            redisJobBeanList.add(jobBean);
            // 将获取到的redis定时任务的过期key 和 对应过期处理放到map中
            SystemConstant.map.put(redisJobs[i], jobBean);
        }
        return redisJobBeanList;
    }


    /**
     * Redis 消息监听器容器.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the redis message listener container
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        return redisMessageListenerContainer;
    }


    /**
     * Redis 定时任务监听器注册为Bean.
     *
     * @param redisMessageListenerContainer the redis message listener container
     * @return the redis event message listener
     */
    @Bean
    public RedisJobEventMessageListener redisEventMessageListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        return new RedisJobEventMessageListener(redisMessageListenerContainer);
    }

}
