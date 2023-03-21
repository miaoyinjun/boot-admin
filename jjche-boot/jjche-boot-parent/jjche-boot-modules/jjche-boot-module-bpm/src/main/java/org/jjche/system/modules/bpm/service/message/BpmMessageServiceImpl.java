package org.jjche.system.modules.bpm.service.message;
import lombok.extern.slf4j.Slf4j;
import org.jjche.system.modules.bpm.service.message.dto.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import org.jjche.system.modules.bpm.service.message.dto.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import org.jjche.system.modules.bpm.service.message.dto.BpmMessageSendWhenTaskCreatedReqDTO;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * BPM 消息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BpmMessageServiceImpl implements BpmMessageService {
//
//    @Resource
//    private SmsSendApi smsSendApi;

    @Resource
    private WebProperties webProperties;

    @Override
    public void sendMessageWhenProcessInstanceApprove(BpmMessageSendWhenProcessInstanceApproveReqDTO reqDTO) {
        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));
//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getStartUserId(),
//                BpmMessageEnum.PROCESS_INSTANCE_APPROVE.getSmsTemplateCode(), templateParams));
    }

    @Override
    public void sendMessageWhenProcessInstanceReject(BpmMessageSendWhenProcessInstanceRejectReqDTO reqDTO) {
        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("reason", reqDTO.getReason());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));
//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getStartUserId(),
//                BpmMessageEnum.PROCESS_INSTANCE_REJECT.getSmsTemplateCode(), templateParams));
    }

    @Override
    public void sendMessageWhenTaskAssigned(BpmMessageSendWhenTaskCreatedReqDTO reqDTO) {
        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("processInstanceName", reqDTO.getProcessInstanceName());
        templateParams.put("taskName", reqDTO.getTaskName());
        templateParams.put("startUserNickname", reqDTO.getStartUserNickname());
        templateParams.put("detailUrl", getProcessInstanceDetailUrl(reqDTO.getProcessInstanceId()));
//        smsSendApi.sendSingleSmsToAdmin(BpmMessageConvert.INSTANCE.convert(reqDTO.getAssigneeUserId(),
//                BpmMessageEnum.TASK_ASSIGNED.getSmsTemplateCode(), templateParams));
    }

    private String getProcessInstanceDetailUrl(String taskId) {
//        return webProperties.getAdminUi().getUrl() + "/bpm/process-instance/detail?id=" + taskId;
        return null;
    }

}
