package com.lyl.bean;

/**
 * @PACKAGE_NAME: com.lyl.bean
 * @ClassName: JobBean
 * @Description: job 任务bean
 * @Date: 2020-06-26 19:42
 **/
public class JobBean {

    /**
     * 任务描述, 任务名
     */
    private String jobName;

    /**
     * 任务运行时间表达式
     */
    private String cronExpression;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务类的全路径
     */
    private String jobClass;


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }
}
