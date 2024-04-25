package org.jjche.bpm.modules.group.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.common.enums.CommonStatusEnum;
import org.jjche.common.serializer.type.JsonLongSetTypeHandler;
import org.jjche.mybatis.base.entity.BaseEntity;

import java.util.Set;

/**
 * Bpm 用户组
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_user_group", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BpmUserGroupDO extends BaseEntity {
    /**
     * 组名
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private CommonStatusEnum status;
    /**
     * 成员用户编号数组
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> memberUserIds;

}
