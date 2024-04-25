package org.jjche.core.alarm.dd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class DingTalkDTO implements Serializable {
    private String msgtype;
    private DingTalkContentDTO text;
}
