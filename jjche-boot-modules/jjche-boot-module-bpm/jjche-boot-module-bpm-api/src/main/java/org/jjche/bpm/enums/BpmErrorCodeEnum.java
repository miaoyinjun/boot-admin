package org.jjche.bpm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.exception.enums.IBaseErrorCodeEnum;

/**
 * DEMO逻辑错误码枚举
 *
 */
@Getter
@AllArgsConstructor
public enum BpmErrorCodeEnum implements IBaseErrorCodeEnum {
    // ==========  通用模块 1-003-000-000 ==========
    SAVE_ERROR(1003000000, "保存失败"),
    DELETE_ERROR(1003000001, "删除失败"),
    UPDATE_ERROR(1003000002, "修改失败"),
    RECORD_NOT_FOUND(1003000003, "记录不存在"),
    FILE_EXPORT_ERROR(1003000004, "文件导出失败"),

    // ========== 任务模块 1-003-001-000 ==========
    TASK_ASSIGNEE_NOT_NULL(1003001000, "任务处理人不能为空"),
    TASK_ASSIGNEE_LEVEL_ZERO(1003001001, "level 必须大于 0"),
    TASK_CHECK_ERROR(1003001002, "审批任务失败，原因：该任务不处于未审批"),
    TASK_NOT_FOUND_ASSIGNEE_ERROR(1003001003, "操作失败，原因：找不到任务的审批人！"),

    // ========== 模型模块 1-003-002-000 ==========
    MODEL_CODE_ALREADY(1003002000, "已经存在流程标识为【{}】的流程"),
    MODEL_NOT_FOUND(1003002001, "流程模型不存在"),

    // ========== 流程模块 1-003-003-000 ==========
    PROCESS_DEFINE_NOT_FOUND(1003003000, "流程定义不存在"),
    PROCESS_DEFINE_ID_NOT_FOUND(1003003001, "流程定义({}) 不存在"),
    PROCESS_DEFINE_EXT_NOT_FOUND(1003003002, "流程定义拓展({}) 不存在"),
    PROCESS_DEFINE_HANG_UP(1003003003, "流程定义处于挂起状态"),
    PROCESS_DEFINE_NO_CHANGE_FOUND(1003003004, "流程定义部署失败，原因：信息未发生变化"),
    PROCESS_DEFINE_CODE_CHECK_FOUND(1003003005, "流程标识格式不正确，需要以字母或下划线开头，后接任意字母、数字、中划线、下划线、句点！"),
    PROCESS_DEFINE_NO_FORM_FOUND(1003003006, "部署流程失败，原因：流程表单未配置，请点击【修改流程】按钮进行配置"),
    PROCESS_DEFINE_CODE_ERROR(1003003006, "流程定义的标识期望是({})，当前是({})，请修改 BPMN 流程图"),
    PROCESS_DEFINE_NAME_ERROR(1003003007, "流程定义的名字期望是({})，当前是({})，请修改 BPMN 流程图"),

    // ========== 表单模块 1-003-004-000 ==========
    FORM_NOT_FOUND(1003004000, "动态表单不存在"),

    // ========== 分配规则模块 1-003-005-000 ==========
    RULE_NOT_FOUND(1003005000, "流程任务分配规则不存在"),
    RULE_SCRIPT_NOT_FOUND(1003005001, "操作失败，原因：任务分配脚本({}) 不存在"),
    RULE_UNKNOWN_FOUND(1003005002, "未知的规则类型({})"),
    RULE_ALREADY_FOUND(1003005003, "流程({}) 的任务({}) 已经存在分配规则"),
    RULE_NOT_ALLOWED_UPDATE(1003005004, "只有流程模型的任务分配规则，才允许被修改"),
    RULE_NOT_ALLOWED_DEPLOY(1003005005, "部署流程失败，原因：用户任务({})未配置分配规则，请点击【修改流程】按钮进行配置"),
    RULE_ACCORD_NOT_FOUND(1003005006, "流程任务({}/{}/{}) 找不到符合的任务规则"),
    RULE_FOUND_MORE(1003005007, "流程任务({}/{}/{}) 找到过多任务规则({})"),

    // ========== 流程实例模块 1-003-006-000 ==========
    INSTANCE_EXT_NOT_FOUND(1003006000, "流程实例拓展({}) 不存在"),
    INSTANCE_CANCEL_ERROR(1003006001, "流程取消失败，流程不处于运行中"),
    INSTANCE_NOT_FOUND(1003006002, "流程实例不存在"),
    INSTANCE_NOT_ALLOWED_CANCEL(1003006003, "流程取消失败，该流程不是你发起的"),
    INSTANCE_NOT_ALLOWED_CHECK(1003006004, "审批任务失败，原因：该任务的审批人不是你"),

    // ========== 用户组模块 1-003-007-000 ==========
    USER_GROUP_NOT_FOUND(1003007000, "用户组不存在"),
    USER_GROUP_DISABLED(1003007001, "名字为【{}】的用户组已被禁用"),

    // ========== OA模块 1-003-008-000 ==========
   OA_NOT_FOUND(1003008000, "请假申请不存在");

    private final int code;
    private final String msg;
}
