package org.jjche.common.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.jjche.common.annotation.QueryCriteria;

import java.io.Serializable;

/**
 * <p>
 * 数据权限规则
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-09-29
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class PermissionDataRuleDTO implements Serializable {

    /**
     * 规则名称
     */
    private String name;

    /**
     * 字段
     */
    private String column;

    /**
     * 条件
     */
    private QueryCriteria.Type condition;

    /**
     * 规则值
     */
    private Object value;

    /**
     * 状态值 1有效 0无效
     */
    private Boolean isActivated;

    /**
     * 权限标识
     */
    private String menuPermission;
}
