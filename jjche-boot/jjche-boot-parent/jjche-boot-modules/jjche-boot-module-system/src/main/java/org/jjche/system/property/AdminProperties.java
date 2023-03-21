package org.jjche.system.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 属性加载
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2021-01-05
 */
@Data
@Component
@ConfigurationProperties(prefix = "jjche")
public class AdminProperties {

    /**
     * 任务
     */
    private AsyncTaskProperties task;

    /**
     * 用户
     */
    private UserProperties user;

    /**
     * 短信
     */
    private AliYunSmsCodeProperties sms;
}
