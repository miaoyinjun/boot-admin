package org.jjche.bpm.modules.definition.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BPM 任务分配规则的类型枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum BpmTaskAssignRuleTypeEnum {

    //角色
    ROLE(10, "角色"),
    //// 包括负责人
    DEPT_MEMBER(20, "部门的成员"),
    //部门的负责人
    DEPT_LEADER(21, "部门的负责人"),
    //岗位
    POST(22, "岗位"),
    //用户
    USER(30, "用户"),
    //用户组
    USER_GROUP(40, "用户组"),
    // 例如说，发起人所在部门的领导、发起人所在部门的领导的领导
    SCRIPT(50, "自定义脚本"),
    ;

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;

}
