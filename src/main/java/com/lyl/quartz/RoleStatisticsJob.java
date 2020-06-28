package com.lyl.quartz;

import com.alibaba.fastjson.JSON;
import com.lyl.bean.Role;
import com.lyl.constant.SystemConstant;
import com.lyl.service.RoleService;
import com.lyl.utils.RedisTemplateUtils;
import com.lyl.utils.SpringContextJobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @PACKAGE_NAME: com.lyl.quartz
 * @ClassName: RoleStatisticsJob
 * @Description: 定时任务
 * @Date: 2020-06-27 13:34
 **/
public class RoleStatisticsJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final String lockKey = "RoleStatisticsJob";
    private final String lockValue = "RoleStatisticsJob";

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.debug("Role Quartz execute start . . . . . . .");

        RedisTemplateUtils redisTemplateUtils = (RedisTemplateUtils) SpringContextJobUtil
                .getBean("redisTemplateUtils");

        // 获取分布式锁
        boolean flag = redisTemplateUtils.tryGetDistributedLock(lockKey, lockValue,
                SystemConstant.DISTRIBUTEDLOCK_EXPIRETIME);

        try {
            if (flag){
                // TODO 业务逻辑
                /**
                 * 通过工具类获取Spring容器中的实例bean
                 * 在quartz框架中，Job 是通过反射出来的实例，不受spring的管理，即使使用@Component注解，
                 * 将其标记为组件类，它也不会被注册到容器中，所以就无法直接通过自动注入service服务层对象等
                 */
                RoleService roleService = (RoleService) SpringContextJobUtil
                        .getBean("roleService");
                Role role =  roleService.findRoleByName("程序员");
                logger.debug("find role by name : " + JSON.toJSONString(role));
            }
        }finally {
            // 保证分布式锁最终被释放
            if (flag){
                redisTemplateUtils.releaseDistributedLock(lockKey, lockValue);
            }
        }

        logger.debug("Role Quartz execute end . . . . . . . .");
    }

}
