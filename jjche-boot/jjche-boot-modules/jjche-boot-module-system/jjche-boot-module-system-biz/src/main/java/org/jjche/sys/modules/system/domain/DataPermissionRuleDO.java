package org.jjche.sys.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>
 * 数据规则
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-10-27
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_data_permission_rule")
public class DataPermissionRuleDO extends BaseEntity {
    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 名称
     */
    private String name;
    /**
     * 条件
     */
    @TableField("`condition`")
    private QueryCriteria.Type condition;
    /**
     * 列名
     */
    @TableField("`column`")
    private String column;
    /**
     * 规则值
     */
    private String value;
    /**
     * 是否有效 1是 0否
     */
    private Boolean isActivated;
}
