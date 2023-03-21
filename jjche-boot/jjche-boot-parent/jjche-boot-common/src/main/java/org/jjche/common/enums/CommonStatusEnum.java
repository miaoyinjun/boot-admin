package org.jjche.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 通用状态枚举
 * </p>
 *
 * @author miaoyj
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {
    ENABLE(0, "开启"),
    DISABLE(1, "关闭");
    private final Integer code;
    private final String desc;
}
