package com.lyl.listener;

import com.lyl.config.RedisMessageListenerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @PACKAGE_NAME: com.lyl.listener
 * @ClassName: RedisJobInitServletContextListener
 * @Description: Redis 定时任务初始化 监听器
 * @Date: 2020-12-21 11:24
 * @Author: 木子雷 公众号
 **/
@WebListener
public class RedisJobInitServletContextListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisMessageListenerConfig redisMessageListenerConfig;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            // redis 定时任务启动
            redisMessageListenerConfig.startRedisJob();
        } catch (Exception e) {
            logger.error("Redis 定时任务启动失败！", e);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("redis job start begin = {}", dateFormat.format(new Date()));

        logger.info("redis 定时任务监听器数据初始化成功。。。");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
