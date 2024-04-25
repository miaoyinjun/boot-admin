package org.jjche.sys.modules.quartz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@Configuration
public class QuartzConfig {

//    /**
//     * 注入scheduler到spring
//     *
//     * @param quartzJobFactory /
//     * @return Scheduler
//     * @throws java.lang.Exception if any.
//     */
//    @Bean(name = "scheduler")
//    public Scheduler scheduler(QuartzJobFactory quartzJobFactory) throws Exception {
//        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
//        factoryBean.setJobFactory(quartzJobFactory);
//        factoryBean.afterPropertiesSet();
//        Scheduler scheduler = factoryBean.getScheduler();
//        scheduler.start();
//        return scheduler;
//    }

    /**
     * 解决Job中注入Spring Bean为null的问题
     */
    @Component("quartzJobFactory")
    public static class QuartzJobFactory extends AdaptableJobFactory {

        private final AutowireCapableBeanFactory capableBeanFactory;

        public QuartzJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
            this.capableBeanFactory = capableBeanFactory;
        }

        @Override
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
            //调用父类的方法，把Job注入到spring中
            Object jobInstance = super.createJobInstance(bundle);
            capableBeanFactory.autowireBean(jobInstance);
            return jobInstance;
        }
    }
}
