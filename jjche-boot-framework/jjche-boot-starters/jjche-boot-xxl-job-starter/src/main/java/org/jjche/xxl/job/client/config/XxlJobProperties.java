package org.jjche.xxl.job.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 配置类
 * </p>
 *
 * @author miaoyj
 * @since 2020-08-19
 * @version 1.0.0-SNAPSHOT
 */
@Data
@ConfigurationProperties(prefix = "jjche.xxljob")
public class XxlJobProperties {
    private String adminAddresses;
    private String accessToken;
    private String appName;
    private String address;
    private String logPath;
    private int logRetentionDays;
    private Boolean enabled;
}
