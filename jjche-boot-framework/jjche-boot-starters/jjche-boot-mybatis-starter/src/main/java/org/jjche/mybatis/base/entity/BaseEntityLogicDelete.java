package org.jjche.mybatis.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 逻辑删除表基类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEntityLogicDelete extends BaseEntity {
    /**
     * 逻辑删除：0:未删除, 1:已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;
}
