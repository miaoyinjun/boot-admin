package org.jjche.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.exception.enums.IBaseErrorCodeEnum;

/**
 * DEMO逻辑错误码枚举
 *
 */
@Getter
@AllArgsConstructor
public enum DemoErrorCodeEnum implements IBaseErrorCodeEnum {
    // ==========  通用模块 1-004-000-000 ==========
    SAVE_ERROR(1004000000, "保存失败"),
    DELETE_ERROR(1004000001, "删除失败"),
    UPDATE_ERROR(1004000002, "修改失败"),
    RECORD_NOT_FOUND(1004000003, "记录不存在"),
    FILE_EXPORT_ERROR(1004000004, "文件导出失败"),

    // ========== 学生模块 1-004-001-000 ==========
    STUDENT_NAME_REPEAT(1004001000, "姓名不能重复");

    private final int code;
    private final String msg;
}
