package org.jjche.system.modules.bpm.dal.dataobject.definition;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
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
     * 编号，自增
     */
    @TableId
    private Long id;
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
    private Integer status;
    /**
     * 成员用户编号数组
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> memberUserIds;

}
