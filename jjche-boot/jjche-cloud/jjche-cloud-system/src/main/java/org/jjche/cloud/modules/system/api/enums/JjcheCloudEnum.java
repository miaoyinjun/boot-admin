package org.jjche.cloud.modules.system.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.base.IBaseEnum;

import java.util.stream.Stream;

/**
 * <p>
 * JjcheCloudEnum
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-26
 */
@Getter
@AllArgsConstructor
public enum JjcheCloudEnum implements IBaseEnum {

    /**
     * 测试
     */
    TEST("1", "测试"),
    ;

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
    public static JjcheCloudEnum resolve(String code) {
        return Stream.of(JjcheCloudEnum.values()).filter(targetEnum -> targetEnum.value.equals(code)).findFirst().orElse(null);
    }
}
