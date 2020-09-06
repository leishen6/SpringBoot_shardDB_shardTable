package com.lyl.quartz;

import cn.hutool.setting.dialect.Props;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @PACKAGE_NAME: com.lyl.quartz
 * @ClassName: StartQuartzJobListener
 * @Description: Scheduler注入到Spring容器，并采用spring容器加载完毕后的监听事件，启动定时任务
 * @Date: 2020-06-26 20:47
 **/
@Configuration
public class StartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private SchedulerQuartzConfig quartzScheduler;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            Props props = new Props("application-quartz.properties");
            boolean flag = props.getBool("default_if_disable_quartz");
            // 在配置文件中获取是否启用定时任务
            if (!flag) {
                quartzScheduler.startJob();
                logger.debug("定时任务已经启动.......");
            }
        } catch (Exception e) {
            logger.error("定时任务启动失败：", e);
        }
    }


    /**
     * 向Spring 容器 初始注入scheduler
     *
     * @return
     * @throws SchedulerException
     */
    @Bean
    public Scheduler scheduler()
            throws SchedulerException {
        SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
        return schedulerFactoryBean.getScheduler();
    }

}
