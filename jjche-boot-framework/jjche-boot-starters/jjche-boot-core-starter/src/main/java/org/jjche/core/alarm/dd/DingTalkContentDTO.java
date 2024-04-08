package org.jjche.core.alarm.dd;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 钉钉告警内容
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-12-16
 */
@Data
public class DingTalkContentDTO implements Serializable {
    private String content;
}
