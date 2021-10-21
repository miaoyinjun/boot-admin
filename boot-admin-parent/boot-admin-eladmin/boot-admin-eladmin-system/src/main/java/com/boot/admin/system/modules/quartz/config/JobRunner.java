package com.boot.admin.system.modules.quartz.config;

import com.boot.admin.system.modules.quartz.domain.QuartzJobDO;
import com.boot.admin.system.modules.quartz.service.QuartzJobService;
import com.boot.admin.system.modules.quartz.utils.QuartzManage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>JobRunner class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(JobRunner.class);
    private final QuartzJobService quartzJobService;
    private final QuartzManage quartzManage;

    /**
     * {@inheritDoc}
     * <p>
     * 项目启动时重新激活启用的定时任务
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        log.info("--------------------注入定时任务---------------------");
        List<QuartzJobDO> quartzJobs = quartzJobService.listIsPauseIsFalse();
        quartzJobs.forEach(quartzManage::addJob);
        log.info("--------------------定时任务注入完成---------------------");
    }
}
