package org.jjche.mybatis.param;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.base.IBaseEnum;

import java.util.stream.Stream;

/**
 * <p>
 * 排序枚举
 * </p>
 *
 * @author miaoyj
 * @since 2021-12-08
 */
@Getter
@AllArgsConstructor
public enum SortEnum implements IBaseEnum {

    /**
     * 主键
     */
    ID_DESC("id DESC", "id倒序"),
    ID_ASC("id ASC", "id正序"),
    ;

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
    public static SortEnum resolve(String code) {
        return Stream.of(SortEnum.values()).filter(targetEnum -> targetEnum.value.equals(code)).findFirst().orElse(null);
    }
}
