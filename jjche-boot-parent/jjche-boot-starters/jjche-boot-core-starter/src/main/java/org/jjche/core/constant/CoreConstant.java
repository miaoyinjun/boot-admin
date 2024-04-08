package org.jjche.core.constant;

/**
 * <p>
 * 告警定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-12-17
 */
public interface CoreConstant {
    /**
     * 属性路径前缀{@value}
     */
    String PROPERTY_CORE_PACKAGE_PREFIX = "jjche.core";

    /**
     * 告警属性路径前缀{@value}
     */
    String PROPERTY_CORE_ALARM_PACKAGE_PREFIX = PROPERTY_CORE_PACKAGE_PREFIX + ".alarm";

    /**
     * 钉钉属性路径前缀{@value}
     */
    String PROPERTY_CORE_ALARM_DING_TALK_PACKAGE_PREFIX = PROPERTY_CORE_ALARM_PACKAGE_PREFIX + ".ding-talk";

    /**
     * 钉钉属性url{@value}
     */
    String PROPERTY_CORE_ALARM_DING_TALK_URL_PACKAGE_PREFIX = PROPERTY_CORE_ALARM_DING_TALK_PACKAGE_PREFIX + ".url";

    /**
     * Constant <code>MSG_WARN_ALARM="没有发现告警配置"</code>
     */
    String MSG_WARN_ALARM = "没有发现告警配置";
}
