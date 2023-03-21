package org.jjche.system.modules.bpm.dal.dataobject.definition;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * 工作流的表单定义
 * 用于工作流的申请表单，需要动态配置的场景
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_form", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BpmFormDO extends BaseEntity {

    /**
     * 表单名
     */
    private String name;
    /**
     * 表单的配置
     */
    private String content;
    /**
     * 备注
     */
    private String remark;

}
