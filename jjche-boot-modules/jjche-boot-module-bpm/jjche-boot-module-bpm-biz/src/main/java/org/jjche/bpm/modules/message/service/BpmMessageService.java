package org.jjche.bpm.modules.message.service;

import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.message.dto.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import org.jjche.bpm.modules.message.dto.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import org.jjche.bpm.modules.message.dto.BpmMessageSendWhenTaskCreatedReqDTO;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

/**
 * BPM 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@RequiredArgsConstructor
public class BpmMessageService {
//
//    @Resource
//    private SmsSendApi smsSendApi;

    private final WebProperties webProperties;

    /**
     * 发送流程实例被通过的消息
     *
     * @param reqDTO 发送信息
     */
    public void sendMessageWhenProcessInstanceApprove(BpmMessageSendWhenProcessInstanceApproveReqDTO reqDTO) {
        Map<String, Object> templateParams = new HashMap<>(2);
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));
//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getStartUserId(),
//                BpmMessageEnum.PROCESS_INSTANCE_APPROVE.getSmsTemplateCode(), templateParams));
    }

    /**
     * 发送流程实例被不通过的消息
     *
     * @param reqDTO 发送信息
     */
    public void sendMessageWhenProcessInstanceReject(BpmMessageSendWhenProcessInstanceRejectReqDTO reqDTO) {
        Map<String, Object> templateParams = new HashMap<>(3);
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("reason", reqDTO.getReason());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));
//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getStartUserId(),
//                BpmMessageEnum.PROCESS_INSTANCE_REJECT.getSmsTemplateCode(), templateParams));
    }

    /**
     * 发送任务被分配的消息
     *
     * @param reqDTO 发送信息
     */
    public void sendMessageWhenTaskAssigned(BpmMessageSendWhenTaskCreatedReqDTO reqDTO) {
        Map<String, Object> templateParams = new HashMap<>(4);
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("taskName", reqDTO.getTaskName());
        templateParams.put("startUserNickname", reqDTO.getStartUserNickname());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));
//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getAssigneeUserId(),
//                BpmMessageEnum.TASK_ASSIGNED.getSmsTemplateCode(), templateParams));
    }

    private String getProcessInstanceDetailUrl(String taskId) {
//        return webProperties.getAdminUi().getUrl() + "/process-instance/detail?id=" + taskId;
        return null;
    }

}
