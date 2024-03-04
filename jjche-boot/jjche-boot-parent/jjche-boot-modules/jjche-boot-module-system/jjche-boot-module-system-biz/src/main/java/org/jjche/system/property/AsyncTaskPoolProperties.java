package org.jjche.system.property;

import lombok.Data;

/**
 * <p>
 * 线程池配置属性类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2021-01-05
 */
@Data
public class AsyncTaskPoolProperties {

    private int corePoolSize;

    private int maxPoolSize;

    private int keepAliveSeconds;

    private int queueCapacity;
}
