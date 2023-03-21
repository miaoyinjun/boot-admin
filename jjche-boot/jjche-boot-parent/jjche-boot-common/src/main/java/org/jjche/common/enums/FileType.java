package org.jjche.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * <p>
 * 文件类型
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-24
 */
@Getter
@AllArgsConstructor
public enum FileType implements IBaseEnum {

    /**
     * 图片
     */
    IMAGE("image", "图片"),
    /**
     * 文档
     */
    TXT("txt", "文档"),
    /**
     * 音乐
     */
    MUSIC("music", "音乐"),
    /**
     * 视频
     */
    VIDEO("video", "视频"),
    /**
     * 其他
     */
    OTHER("other", "其他"),
    ;

    private final String value;
    private final String desc;

    /**
     * <p>
     * 根据code获取枚举
     * </p>
     *
     * @param code 标识
     * @return 枚举
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FileType resolve(String code) {
        return Stream.of(FileType.values()).filter(targetEnum -> targetEnum.value.equals(code)).findFirst().orElse(null);
    }
}
