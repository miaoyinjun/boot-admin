package org.jjche.sys.modules.quartz.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.log.StaticLog;
import org.jjche.cache.lock.client.RedissonLockClient;
import org.jjche.cache.service.RedisService;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.core.util.SpringContextHolder;
import org.jjche.sys.modules.quartz.domain.QuartzJobDO;
import org.jjche.sys.modules.quartz.domain.QuartzLogDO;
import org.jjche.sys.modules.quartz.mapper.QuartzLogMapper;
import org.jjche.sys.modules.quartz.service.QuartzJobService;
import org.jjche.sys.modules.tool.api.vo.EmailVO;
import org.jjche.sys.modules.tool.service.EmailService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 参考人人开源，https://gitee.com/renrenio/renren-security
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@Async
@SuppressWarnings({"unchecked", "all"})
public class ExecutionJob extends QuartzJobBean {

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeInternal(JobExecutionContext context) {
        // 获取任务
        QuartzJobDO quartzJob = (QuartzJobDO) context.getMergedJobDataMap().get(QuartzJobDO.JOB_KEY);
        QuartzManage quartzManage = SpringContextHolder.getBean(QuartzManage.class);
        RedissonLockClient redissonLockClient = quartzManage.redissonLockClient;
        Long id = quartzJob.getId();
        String uuid = quartzJob.getUuid();
        if (StrUtil.isBlank(uuid)) {
            uuid = String.valueOf(id);
        }
        uuid = QuartzJobDO.JOB_KEY + "_LOCK:" + uuid;
        //分布式锁
        try {
            if (redissonLockClient.tryLock(uuid)) {
                TimeUnit.SECONDS.sleep(1);
                executionJob(quartzJob, uuid);
            }
        } catch (Exception e) {
            StaticLog.error("executionJob:{},", e);
        } finally {
            if (redissonLockClient.isLocked(uuid) && redissonLockClient.isHeldByCurrentThread(uuid)) {
                redissonLockClient.unlock(uuid);
            }
        }
    }

    private void executionJob(QuartzJobDO quartzJob, String uuid) {
        // 创建单个线程
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // 获取spring bean
        QuartzLogMapper quartzLogMapper = SpringContextHolder.getBean(QuartzLogMapper.class);
        QuartzJobService quartzJobService = SpringContextHolder.getBean(QuartzJobService.class);
        RedisService redisService = SpringContextHolder.getBean(RedisService.class);
        QuartzLogDO log = new QuartzLogDO();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            Future<?> future = executor.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态
            log.setIsSuccess(true);
            StaticLog.info("任务执行成功，任务名称：" + quartzJob.getJobName() + ", 执行时间：" + times + "毫秒");
            // 判断是否存在子任务
            if (StrUtil.isNotBlank(quartzJob.getSubTask())) {
                String subTask = quartzJob.getSubTask();
                if (StrUtil.isNotBlank(subTask)) {
                    String[] tasks = subTask.split("[,，]");
                    // 执行子任务
                    quartzJobService.executionSubJob(tasks);
                }
            }
        } catch (Exception e) {
            StaticLog.error("任务执行失败，任务名称：" + quartzJob.getJobName());
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 0：成功 1：失败
            log.setIsSuccess(false);
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            // 任务如果失败了则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setIsPause(false);
                //更新状态
                quartzJobService.updateIsPause(quartzJob);
            }
            if (StrUtil.isNotBlank(quartzJob.getEmail())) {
                EmailService emailService = SpringContextHolder.getBean(EmailService.class);
                // 邮箱报警
                EmailVO emailVo = taskAlarm(quartzJob, ThrowableUtil.getStackTrace(e));
                emailService.send(emailVo, emailService.find());
            }
        } finally {
            quartzLogMapper.insert(log);
            executor.shutdown();
        }
    }


    private EmailVO taskAlarm(QuartzJobDO quartzJob, String msg) {
        EmailVO emailVo = new EmailVO();
        emailVo.setSubject("定时任务【" + quartzJob.getJobName() + "】执行失败，请尽快处理！");
        Map<String, Object> data = new HashMap<>(16);
        data.put("task", quartzJob);
        data.put("msg", msg);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/taskAlarm.ftl");
        emailVo.setContent(template.render(data));
        List<String> emails = Arrays.asList(quartzJob.getEmail().split("[,，]"));
        emailVo.setTos(emails);
        return emailVo;
    }
}
