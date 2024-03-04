package org.jjche.system.config.thread;

import cn.hutool.log.StaticLog;
import org.jjche.system.property.AdminProperties;
import org.jjche.system.property.AsyncTaskPoolProperties;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务线程池装配类
 *
 * @author https://juejin.im/entry/5abb8f6951882555677e9da2
 * @version 1.0.8-SNAPSHOT
 * @since 2019年10月31日15:06:18
 */
@Configuration
public class AsyncTaskExecutePool implements AsyncConfigurer {

    /**
     * 注入配置类
     */
    private final AdminProperties adminConfig;

    /**
     * <p>Constructor for AsyncTaskExecutePool.</p>
     *
     * @param adminConfig a {@link AsyncTaskPoolProperties} object.
     */
    public AsyncTaskExecutePool(AdminProperties adminConfig) {
        this.adminConfig = adminConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Executor getAsyncExecutor() {
        AsyncTaskPoolProperties config = adminConfig.getTask().getPool();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(config.getCorePoolSize());
        //最大线程数
        executor.setMaxPoolSize(config.getMaxPoolSize());
        //队列容量
        executor.setQueueCapacity(config.getQueueCapacity());
        //活跃时间
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        //线程名字前缀
        executor.setThreadNamePrefix("jjche-async-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            StaticLog.error("====" + throwable.getMessage() + "====", throwable);
            StaticLog.error("exception method:" + method.getName());
        };
    }
}
