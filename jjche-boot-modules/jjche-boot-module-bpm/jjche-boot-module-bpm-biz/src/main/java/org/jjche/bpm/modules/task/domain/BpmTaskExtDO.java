package org.jjche.bpm.modules.task.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jjche.mybatis.base.entity.BaseEntity;
import org.jjche.bpm.modules.task.enums.BpmProcessInstanceResultEnum;

import java.sql.Timestamp;

/**
 * Bpm 流程任务的拓展表
 * 主要解决 Flowable Task 和 HistoricTaskInstance 不支持拓展字段，所以新建拓展表
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_task_ext", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmTaskExtDO extends BaseEntity {

    /**
     * 任务的审批人
     *
     * 冗余 Task 的 assignee 属性
     */
    private Long assigneeUserId;
    /**
     * 任务的名字
     *
     * 冗余 Task 的 name 属性，为了筛选
     */
    private String name;
    /**
     * 任务的编号
     *
     * 关联 Task 的 id 属性
     */
    private String taskId;
//    /**
//     * 任务的标识
//     *
//     * 关联 {@link Task#getTaskDefinitionKey()}
//     */
//    private String definitionKey;
    /**
     * 任务的结果
     *
     * 枚举 {@link BpmProcessInstanceResultEnum}
     */
    private Integer result;
    /**
     * 审批建议
     */
    private String reason;
    /**
     * 任务的结束时间
     *
     * 冗余 HistoricTaskInstance 的 endTime  属性
     */
    private Timestamp endTime;

    /**
     * 流程实例的编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 流程定义的编号
     *
     * 关联 ProcessDefinition 的 id 属性
     */
    private String processDefinitionId;

}
