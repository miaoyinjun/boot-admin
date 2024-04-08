package org.jjche.sys.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jjche.common.exception.enums.IBaseErrorCodeEnum;

/**
 * 系统错误码枚举
 *
 */
@Getter
@AllArgsConstructor
public enum SysErrorCodeEnum implements IBaseErrorCodeEnum {
    // ==========  通用模块 1-002-000-000 ==========
    SAVE_ERROR(1002000000, "保存失败"),
    DELETE_ERROR(1002000001, "删除失败"),
    UPDATE_ERROR(1002000002, "修改失败"),
    RECORD_NOT_FOUND(1002000003, "记录不存在"),
    FILE_EXPORT_ERROR(1002000004, "文件导出失败"),
    ID_ALREADY_ERROR(1002000005, "新增不允许出现id字段"),

    // ========== 认证模块 1-002-001-000 ==========
    AUTH_VALID_NOT_CONFIG_ERROR(1002001000, "前端需要配置验证方式-手输或滑动"),
    AUTH_VALID_NOT_FOUND_ERROR(1002001001, "验证码不存在或已过期"),
    AUTH_VALID_ERROR(1002001002, "验证码错误"),
    AUTH_VALID_FAILED_ERROR(1002001003, "验证失败"),
    AUTH_USER_PHONE_NOT_FOUND_ERROR(1002001004,"未找到该手机号用户"),
    AUTH_SMS_NOT_CONFIG_ERROR(1002001005,"请先配置短信通道"),
    AUTH_PHONE_VALID_NOT_FOUND_ERROR(1002001006,"手机验证码不存在或已过期"),
    AUTH_PHONE_VALID_ALWAYS_ERROR(1002001006,"手机验证码连续错误，请重新获取"),
    AUTH_PHONE_VALID_ERROR(1002001003,"手机验证码错误"),

    // ========== 用户模块 1-002-002-000 ==========
    USER_OLD_PWD_ERROR(1002002000, "修改失败，旧密码错误"),
    USER_OLD_NEW_PWD_SAME_ERROR(1002002001, "新密码不能与旧密码相同"),
    USER_PWD_ERROR(1002002002, "密码错误"),
    USER_USERNAME_ALREADY_ERROR(1002002003, "用户名【{}】已存在"),
    USER_EMAIL_ALREADY_ERROR(1002002004, "邮箱【{}】已存在"),
    USER_PHONE_ALREADY_ERROR(1002002005, "手机号【{}】已存在"),
    USER_NOT_FOUND_ERROR(1002002006, "用户不存在"),
    USER_NOT_ALLOWED_UPDATE_OTHER_ERROR(1002002007, "不能修改他人资料"),
    USER_PWD_LENGTH_ERROR(1002002008, "密码长度不得小于{}位，大于{}位"),
    USER_PWD_UC_ERROR(1002002009, "密码必须包含大写字母"),
    USER_PWD_LC_ERROR(1002002010, "密码必须包含小写字母"),
    USER_PWD_NUMBER_ERROR(1002002011, "密码必须包含数字"),
    USER_PWD_SC_ERROR(1002002012, "密码必须包含特殊符号"),

    // ========== 角色模块 1-002-003-000 ==========
    ROLE_LEVEL_ERROR(1002003000, "权限不足，你的角色级别【{}】，低于操作的角色级别【{}】"),
    ROLE_LEVEL_DEL_ERROR(1002003001, "角色权限不足，不能删除【{}】"),
    ROLE_NAME_ALREADY_ERROR(1002003002, "名称【{}】已存在"),
    ROLE_CODE_ALREADY_ERROR(1002003003, "编码【{}】已存在"),
    ROLE_USER_NOT_ALLOWED_DEL_ERROR(1002003004, "所选角色存在用户关联，请解除关联再试！"),
    ROLE_NOT_FOUND_ERROR(1002003005, "角色不存在"),

    // ========== 部门模块 1-002-004-000 ==========
    DEPT_NOT_FOUND_ERROR(1002004000, "未找到部门"),
    DEPT_NOT_ALLOWED_PARENT_SELF_ERROR(1002004001, "上级不能为自己"),
    DEPT_ALREADY_USER_NOT_DEL_ERROR(1002004002, "所选部门存在用户关联，请解除后再试！"),
    DEPT_ALREADY_ROLE_NOT_DEL_ERROR(1002004003, "所选部门存在角色关联，请解除后再试！"),

    // ========== 岗位模块 1-002-005-000 ==========
    JOB_NAME_ALREADY_ERROR(1002005000, "【{}】已存在"),
    JOB_ALREADY_USER_NOT_DEL_ERROR(1002005001, "所选的岗位中存在用户关联，请解除关联再试！"),
    JOB_NOT_FOUND_ERROR(1002005001, "当前岗位不存在"),

    // ========== 菜单模块 1-002-006-000 ==========
    MENU_TITLE_ALREADY_ERROR(1002006000, "【{}】已存在"),
    MENU_NAME_ALREADY_ERROR(1002006001, "【{}】已存在"),
    MENU_URL_PREFIX_ERROR(1002006002, "外链必须以http://或者https://开头"),
    MENU_NOT_ALLOWED_SELF_ERROR(1002006003, "上级不能为自己"),

    // ========== 文件模块 1-002-007-000 ==========
    FILE_PIC_ERROR(1002007000, "请选择图片"),
    FILE_UPLOAD_ERROR(1002007001, "上传失败"),

    // ========== 字典模块 1-002-008-000 ==========
    DICT_NOT_FOUND_ERROR(1002008000, "未找到字典"),
    DICT_DETAIL_NOT_FOUND_ERROR(1002008001, "当前字典数据不存在"),

    // ========== 定时模块 1-002-009-000 ==========
    QUARTZ_CRON_ERROR(1002009000, "cron表达式格式错误"),
    QUARTZ_SUB_TASK_ADD_ERROR(1002009001, "子任务中不能添加当前任务ID"),
    QUARTZ_TASK_ADD_ERROR(1002009002, "创建定时任务失败"),
    QUARTZ_TASK_UPDATE_ERROR(1002009003, "更新定时任务失败"),
    QUARTZ_TASK_DEL_ERROR(1002009004, "删除定时任务失败"),
    QUARTZ_TASK_RESUME_ERROR(1002009005, "恢复定时任务失败"),
    QUARTZ_TASK_START_ERROR(1002009006, "定时任务执行失败"),
    QUARTZ_TASK_STOP_ERROR(1002009007, "定时任务暂停失败"),


    // ========== 应用密钥模块 1-002-010-000 ==========
    SECURITY_APP_KEY_NAME_REPEAT(1002010000, "应用名称【{}】不能重复"),
    SECURITY_APP_KEY_ID_REPEAT(1002010001, "应用ID【{}】不能重复"),

    // ========== 代码生成模块 1-002-012-000 ==========
    GENERATOR_CODE_NOT_ALLOWED(1002012000, "非开发环境不允许生成代码，请选择预览或者下载查看！"),
    GENERATOR_CODE_BEFORE_CONFIG(1002012001, "请先配置生成器"),
    GENERATOR_CODE_ERROR(1002012002, "生成失败，请手动处理已生成的文件"),
    GENERATOR_CODE_ZIP_ERROR(1002012003, "打包失败"),
    GENERATOR_CODE_NO_ITEM_ERROR(1002012004, "没有这个选项"),

    // ========== 数据库模块 1-002-013-000 ==========
    DB_EXECUTE_ERROR(1002013000, "执行失败"),

    // ========== 应用模块 1-002-014-000 ==========
    APP_FILE_UPLOAD_ERROR(1002014000, "文件只能上传在opt目录或者home目录"),
    APP_FILE_DEPLOY_ERROR(1002014001, "文件只能部署在opt目录或者home目录"),
    APP_FILE_BAK_ERROR(1002014002, "文件只能备份在opt目录或者home目录"),
    APP_DEPLOY_NOT_FOUND_ERROR(1002014003, "部署信息不存在"),
    APP_PACKAGE_NOT_FOUND_ERROR(1002014004, "包对应应用信息不存在"),
    APP_NOT_FOUND_ERROR(1002014005, "应用【{}】信息不存在"),
    APP_IP_NOT_FOUND_ERROR(1002014006, "IP【{}】对应服务器信息不存在"),

    // ========== 支付模块 1-001-012-000 ==========
    PAY_VALID_ERROR(1002012000, "内容验签失败"),

    // ========== 邮件模块 1-001-014-000 ==========
    EMAIL_NOT_CONFIG_ERROR(1002014000, "请先配置，再操作"),
    EMAIL_SEND_FAILED_ERROR(1002014001, "发送失败"),

    // ========== 七牛模块 1-001-015-000 ==========
    QINIU_NOT_CONFIG_ERROR(1002015000, "请先配置，再操作"),
    QINIU_UPLOAD_FAILED_ERROR(1002015001, "上传失败"),

    // ========== 版本模块 1-001-016-000 ==========
    VERSION_NAME_ALREADY_ERROR(1002016000, "名称不能重复");

    private final int code;
    private final String msg;
}
