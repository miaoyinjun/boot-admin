package org.jjche.bpm.modules.message.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * BPM 发送流程实例被通过 Request DTO
 */
@Data
public class BpmMessageSendWhenProcessInstanceApproveReqDTO {

    /**
     * 流程实例的编号
     */
    @NotBlank(message = "流程实例的编号不能为空")
    private String processInstanceId;
    /**
     * 流程实例的名字
     */
    @NotBlank(message = "流程实例的名字不能为空")
    private String processInstanceName;
    @NotNull(message = "发起人的用户编号")
    private Long startUserId;

}
