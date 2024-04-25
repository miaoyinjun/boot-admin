package org.jjche.shardingsphere.env;

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
public class ShardingSphereEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for ShardingSphereEnvironmentPostProcessor</p>
     */
    public ShardingSphereEnvironmentPostProcessor() {
        super.setYmlName("shardingsphere.yml");
    }
}