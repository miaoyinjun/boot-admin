package org.jjche.cloud.env;

import org.jjche.common.yml.CoreEnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * <p>
 * 自定义加载yml类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CloudEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for MybatisEnvironmentPostProcessor</p>
     */
    public CloudEnvironmentPostProcessor() {
        super.setYmlName("cloud.yml");
    }
}
