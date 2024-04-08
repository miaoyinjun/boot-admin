package org.jjche.mybatis.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 表基类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @OrderBy
    private Long id;

    /**
     * 创建时间
     */
    private Timestamp gmtCreate;

    /**
     * 修改时间
     */
    private Timestamp gmtModified;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 修改者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;
}
