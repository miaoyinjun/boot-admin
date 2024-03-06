package org.jjche.demo.modules.provider.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.base.IBaseEnum;

import java.util.stream.Stream;

/**
 * <p>
 * 课程类型,102:图文,103:音频,104:视频,105:外链
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Getter
@AllArgsConstructor
public enum ProviderCourseEnum implements IBaseEnum {

    /**
     * 图文
     */
    PICTURE("102", "图文"),
    /**
     * 音频
     */
    AUDIO("103", "音频"),
    /**
     * 视频
     */
    VIDEO("104", "视频"),
    /**
     * 外链
     */
    URL("105", "外链"),
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
    public static ProviderCourseEnum resolve(String code) {
        return Stream.of(ProviderCourseEnum.values()).filter(targetEnum -> targetEnum.value.equals(code)).findFirst().orElse(null);
    }
}
