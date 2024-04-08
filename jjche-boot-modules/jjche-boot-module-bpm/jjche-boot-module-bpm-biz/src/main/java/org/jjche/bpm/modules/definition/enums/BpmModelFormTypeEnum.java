package org.jjche.bpm.modules.definition.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 模型的表单类型的枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum BpmModelFormTypeEnum {

    // 对应 BpmFormDO
    NORMAL(10, "流程表单"),
    // 业务自己定义的表单，自己进行数据的存储
    CUSTOM(20, "业务表单")
    ;

    private final Integer type;
    private final String desc;
}
