package org.jjche.bpm.modules.bpm.dal.dataobject.definition;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import org.jjche.mybatis.base.entity.BaseEntity;

import java.util.List;

/**
 * 工作流的表单定义
 * 用于工作流的申请表单，需要动态配置的场景
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_form", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmFormDO extends BaseEntity {

    /**
     * 表单名
     */
    private String name;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 表单的配置
     */
    private String conf;
    /**
     * 表单项的数组
     *
     * 目前直接将 https://github.com/JakHuang/form-generator 生成的 JSON 串，直接保存
     * 定义：https://github.com/JakHuang/form-generator/issues/46
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> fields;
    /**
     * 备注
     */
    private String remark;

}
