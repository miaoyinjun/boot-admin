package org.jjche.sys.property;

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
public class AsyncTaskProperties {

    private AsyncTaskPoolProperties pool;
}
