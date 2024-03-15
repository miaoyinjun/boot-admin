package org.jjche.system.modules.quartz.utils;

import cn.hutool.log.StaticLog;
import org.jjche.cache.lock.client.RedissonLockClient;
import org.jjche.system.modules.quartz.domain.QuartzJobDO;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * <p>QuartzManage class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@Component
public class QuartzManage {

    private static final String JOB_NAME = "TASK_";
    @Resource
    public RedissonLockClient redissonLockClient;
    @Resource
    private Scheduler scheduler;

    /**
     * <p>addJob.</p>
     *
     * @param quartzJob a {@link QuartzJobDO} object.
     */
    public void addJob(QuartzJobDO quartzJob) {
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ExecutionJob.class).
                    withIdentity(JOB_NAME + quartzJob.getId()).build();

            //通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = newTrigger()
                    .withIdentity(JOB_NAME + quartzJob.getId())
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression()))
                    .build();

            cronTrigger.getJobDataMap().put(QuartzJobDO.JOB_KEY, quartzJob);

            //重置启动时间
            ((CronTriggerImpl) cronTrigger).setStartTime(new Date());

            //执行定时任务
            scheduler.scheduleJob(jobDetail, cronTrigger);

            // 暂停任务
            if (quartzJob.getIsPause()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            StaticLog.error("创建定时任务失败", e);
            throw new IllegalArgumentException("创建定时任务失败");
        }
    }

    /**
     * 更新job cron表达式
     *
     * @param quartzJob /
     */
    public void updateJobCron(QuartzJobDO quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl) trigger).setStartTime(new Date());
            trigger.getJobDataMap().put(QuartzJobDO.JOB_KEY, quartzJob);

            scheduler.rescheduleJob(triggerKey, trigger);
            // 暂停任务
            if (quartzJob.getIsPause()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            StaticLog.error("更新定时任务失败", e);
            throw new IllegalArgumentException("更新定时任务失败");
        }

    }

    /**
     * 删除一个job
     *
     * @param quartzJob /
     */
    public void deleteJob(QuartzJobDO quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            StaticLog.error("删除定时任务失败", e);
            throw new IllegalArgumentException("删除定时任务失败");
        }
    }

    /**
     * 恢复一个job
     *
     * @param quartzJob /
     */
    public void resumeJob(QuartzJobDO quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
            }
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            StaticLog.error("恢复定时任务失败", e);
            throw new IllegalArgumentException("恢复定时任务失败");
        }
    }

    /**
     * 立即执行job
     *
     * @param quartzJob /
     */
    public void runJobNow(QuartzJobDO quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(QuartzJobDO.JOB_KEY, quartzJob);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.triggerJob(jobKey, dataMap);
        } catch (Exception e) {
            StaticLog.error("定时任务执行失败", e);
            throw new IllegalArgumentException("定时任务执行失败");
        }
    }

    /**
     * 暂停一个job
     *
     * @param quartzJob /
     */
    public void pauseJob(QuartzJobDO quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            StaticLog.error("定时任务暂停失败", e);
            throw new IllegalArgumentException("定时任务暂停失败");
        }
    }
}
