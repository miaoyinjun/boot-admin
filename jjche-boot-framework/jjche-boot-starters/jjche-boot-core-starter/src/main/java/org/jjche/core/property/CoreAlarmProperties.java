package org.jjche.core.property;

import lombok.Data;

/**
 * <p>
 * 告警前缀配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-21
 */
@Data
public class CoreAlarmProperties {
    private CoreAlarmDingTalkProperties dingTalk;
}
