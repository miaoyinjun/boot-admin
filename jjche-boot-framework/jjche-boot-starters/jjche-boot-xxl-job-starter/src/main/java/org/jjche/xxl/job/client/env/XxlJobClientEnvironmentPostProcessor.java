package org.jjche.xxl.job.client.env;


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
public class XxlJobClientEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for SwaggerEnvironmentPostProcessor</p>
     */
    public XxlJobClientEnvironmentPostProcessor() {
        super.setYmlName("xxl-job-client.yml");
    }
}
