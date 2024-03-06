package org.jjche.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.base.IBaseEnum;

import java.util.stream.Stream;

/**
 * <p>
 * 通用状态枚举
 * </p>
 *
 * @author miaoyj
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements IBaseEnum {
    ENABLE("1", "开启"),
    DISABLE("0", "关闭");

    @EnumValue
    private final String value;
    private final String desc;

    /**
     * <p>
     * 根据code获取枚举
     * </p>
     *
     * @param code 标识
     *
     * @return 枚举
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CommonStatusEnum resolve(String code) {
        return Stream.of(CommonStatusEnum.values()).filter(targetEnum -> targetEnum.value.equals(code)).findFirst().orElse(null);
    }
}
