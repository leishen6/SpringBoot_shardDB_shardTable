package com.lyl.quartz;

import cn.hutool.setting.dialect.Props;
import com.lyl.bean.JobBean;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @PACKAGE_NAME: com.lyl.quartz
 * @ClassName: QuartzScheduler
 * @Description:  定时任务 quartz 配置类
 * @Date: 2020-06-26 17:52
 **/
@Configuration
public class SchedulerQuartzConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 任务调度器
    @Autowired
    private Scheduler scheduler;

    /**
     * 开始执行所有定时任务
     *
     * @throws SchedulerException
     */
    public void startJob() throws Exception {
        List<JobBean> jobBeanList = getJobBeanByConfig();

        if (jobBeanList != null && jobBeanList.size()>0){
            for (JobBean jobBean : jobBeanList){
                registerJobToScheduler(scheduler, jobBean);
            }
        }
        scheduler.start();
    }


    /**
     *  获取定时任务配置参数
     * @return
     * @throws Exception
     */
    private List<JobBean> getJobBeanByConfig() throws Exception {
        Props props = new Props("application-quartz.properties");
        String jobList = props.getStr("jobList");
        if (jobList == null || "".equals(jobList)){
            throw new Exception("定时任务集合 jobList 未配置 . . . . . .");
        }
        List<JobBean> jobBeanList = new ArrayList<>();
        String[] jobs = jobList.split(",");

        for (int i = 0 ; i < jobs.length; i++){
            JobBean jobBean = new JobBean();
            jobBean.setJobClass(props.getStr(jobs[i] + ".jobClass"));
            jobBean.setJobGroup(jobs[i]);
            jobBean.setJobName(props.getStr(jobs[i] + ".name"));
            jobBean.setCronExpression(props.getStr(jobs[i] + ".cron"));
            jobBeanList.add(jobBean);
        }
        return jobBeanList;
    }



    /**
     *  将定时任务与其对应的触发器注册到调度器Scheduler中
     * @param scheduler
     * @param jobBean
     */
    private void registerJobToScheduler(Scheduler scheduler, JobBean jobBean) {

        try {
            Class<Job> jobClass = (Class<Job>) Class.forName(jobBean.getJobClass());

            // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
            // JobDetail 是具体Job实例
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobBean.getJobName(), jobBean.getJobGroup()).build();

            // 基于表达式构建触发器的执行时间配置
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                    .cronSchedule(jobBean.getCronExpression());

            // CronTrigger表达式触发器 继承于Trigger
            // TriggerBuilder 用于构建《 触发器实例 》
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobBean.getJobName(), jobBean.getJobGroup())
                    .withSchedule(cronScheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, cronTrigger);

        }catch (Exception e) {
            logger.error("\n定时任务注册到调度器失败：" , e);
        }

    }





    /**
     * 获取Job信息
     *
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    private String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     *
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     */
    private boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    private void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    private void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    private void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    private void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    private void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }
}
