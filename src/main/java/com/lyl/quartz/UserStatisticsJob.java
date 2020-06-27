package com.lyl.quartz;

import com.alibaba.fastjson.JSON;
import com.lyl.bean.User;
import com.lyl.service.UserService;
import com.lyl.utils.RedisTemplateUtils;
import com.lyl.utils.SpringContextJobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @PACKAGE_NAME: com.lyl.quartz
 * @ClassName: UserStatisticsJob
 * @Description:  定时任务
 * @Date: 2020-06-26 17:34
 **/
public class UserStatisticsJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.debug("User Quartz execute start . . . . . . .");

        RedisTemplateUtils redisTemplateUtils = (RedisTemplateUtils) SpringContextJobUtil
                .getBean("redisTemplateUtils");

        // 获取分布式锁
        boolean flag = redisTemplateUtils.tryGetDistributedLock("UserStatisticsJob", "UserStatisticsJob", 1000);

        try {
            if (flag){
                logger.debug("User tryGetDistributedLock success  . . . . ");
                // TODO 业务逻辑
                /**
                 * 通过工具类获取Spring容器中的实例bean
                 * 在quartz框架中，Job 是通过反射出来的实例，不受spring的管理，即使使用@Component注解，
                 * 将其标记为组件类，它也不会被注册到容器中，所以就无法直接通过自动注入service服务层对象等
                 */
                UserService userService = (UserService) SpringContextJobUtil
                        .getBean("userService");
                List<User> users =  userService.findAll();
                logger.debug("find All user : " + JSON.toJSONString(users));
            }
        }finally {
            // 保证分布式锁最终被释放
            if (flag){
                redisTemplateUtils.releaseDistributedLock("UserStatisticsJob", "UserStatisticsJob");
            }
        }

        logger.debug("User Quartz execute end . . . . . . . .");
    }

}
