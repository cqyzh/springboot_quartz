package com.yzh.demo.manager;

import com.yzh.demo.entity.SysTask;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author : yzh
 * @date : 2020-09-27 10:15
 **/
@Component
public class QuartzManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzManager.class);

    @Autowired
    private Scheduler scheduler;

    /**
     * 添加并启动任务
     * @param task
     */
    @SuppressWarnings("unchecked")
    public void addJob(SysTask task) throws Exception {
        //创建jobDetail实例，绑定job实现类
        Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName(task.getBeanClass()).newInstance()
                .getClass());
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(task.getJobName(), task.getJobGroup()).build();
        //定义调度触发规则
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup())// 触发器key
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression())).startNow().build();
        //把作业和触发器注册到任务调度中
        scheduler.scheduleJob(jobDetail,trigger);
        //启动
        if (!scheduler.isShutdown()){
            scheduler.start();
        }
    }

    /**
     * 暂停一个任务
     * @param task
     */
    public void pauseJob(SysTask task) throws SchedulerException {
        LOGGER.info("暂停任务：【{}】-【{}】",task.getJobName(),task.getJobGroup());
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个任务
     * @param task
     */
    public void resumeJob(SysTask task) throws SchedulerException {
        LOGGER.info("恢复任务：【{}】-【{}】",task.getJobName(),task.getJobGroup());
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个任务
     * @param task
     * @throws SchedulerException
     */
    public void deleteJob(SysTask task) throws SchedulerException {
        LOGGER.info("删除任务：【{}】-【{}】",task.getJobName(),task.getJobGroup());
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.deleteJob(jobKey);
    }

    /**
     * 立即触发一个任务
     */
    public void runJobNow(SysTask task) throws SchedulerException {
        LOGGER.info("立即触发任务：【{}】-【{}】",task.getJobName(),task.getJobGroup());
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新任务的表达式
     * @param task
     */
    public void updateJonCron(SysTask task) throws SchedulerException {
        LOGGER.info("更新任务的表达式：【{}】-【{}】-【{}】",task.getJobName(),task.getJobGroup(),task.getCronExpression());
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     *  获取所有计划中的任务列表
     */
    public List<SysTask>findAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<SysTask>list = new ArrayList<>();

        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

            for (Trigger trigger : triggers) {
                SysTask sysTask = new SysTask();
                sysTask.setJobName(jobKey.getName());
                sysTask.setJobGroup(jobKey.getGroup());
                sysTask.setDescription("触发器："+trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                sysTask.setJobStatus(triggerState.name());

                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    sysTask.setCronExpression(cronExpression);
                }
                list.add(sysTask);
            }
        }
        return list;
    }
}
