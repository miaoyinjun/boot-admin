package org.jjche.log.env;

import org.jjche.common.yml.CoreEnvironmentPostProcessor;

/**
 * <p>
 * 自定义加载yml类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class LogHttpEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for LogHttpEnvironmentPostProcessor</p>
     */
    public LogHttpEnvironmentPostProcessor() {
        super.setYmlName("log.yml");
    }
}
