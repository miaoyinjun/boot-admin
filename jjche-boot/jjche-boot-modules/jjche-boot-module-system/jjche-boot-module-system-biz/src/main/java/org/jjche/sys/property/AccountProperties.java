package org.jjche.sys.property;

import lombok.Data;

/**
 * <p>
 * 账号配置属性类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2021-01-05
 */
@Data
public class AccountProperties {
    /**
     * 多少天账号过期
     */
    private Integer expiredDays;
}
