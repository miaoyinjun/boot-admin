package org.jjche.core.property;

import lombok.Data;
import org.jjche.core.constant.CoreConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 告警配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-12-17
 */
@Data
@Component
@ConfigurationProperties(prefix = CoreConstant.PROPERTY_CORE_PACKAGE_PREFIX)
public class CoreProperties {
    /**
     * api
     */
    private CoreApiProperties api;
    /**
     * 告警
     */
    private CoreAlarmProperties alarm;
}
