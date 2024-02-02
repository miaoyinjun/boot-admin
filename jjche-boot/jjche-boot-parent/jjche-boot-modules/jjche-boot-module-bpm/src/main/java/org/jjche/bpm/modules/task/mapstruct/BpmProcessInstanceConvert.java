package org.jjche.bpm.modules.task.mapstruct;

import cn.hutool.core.util.NumberUtil;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.jjche.bpm.modules.message.api.dto.BpmMessageSendWhenProcessInstanceApproveReqDTO;
import org.jjche.bpm.modules.message.api.dto.BpmMessageSendWhenProcessInstanceRejectReqDTO;
import org.jjche.bpm.config.bpm.core.event.BpmProcessInstanceResultEvent;
import org.jjche.bpm.modules.task.api.vo.instance.BpmProcessInstancePageItemRespVO;
import org.jjche.common.dto.DeptSmallDto;
import org.jjche.common.dto.UserVO;
import org.jjche.common.param.MyPage;
import org.jjche.bpm.modules.definition.domain.BpmProcessDefinitionExtDO;
import org.jjche.bpm.modules.task.domain.BpmProcessInstanceExtDO;
import org.jjche.bpm.modules.task.api.vo.instance.BpmProcessInstanceRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;

/**
 * 流程实例 Convert
 *
 * @author 芋道源码
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmProcessInstanceConvert {
    default MyPage<BpmProcessInstancePageItemRespVO> convertPage(MyPage<BpmProcessInstanceExtDO> page,
                                                                 Map<String, List<Task>> taskMap) {
        List<BpmProcessInstancePageItemRespVO> list = convertList(page.getRecords());
        list.forEach(respVO -> respVO.setTasks(convertList2(taskMap.get(respVO.getId()))));
        MyPage myPage = new MyPage();
        myPage.setTotal(page.getTotal());
        myPage.setNewRecords(list);
        return myPage;
    }

    List<BpmProcessInstancePageItemRespVO> convertList(List<BpmProcessInstanceExtDO> list);

    @Mapping(source = "processInstanceId", target = "id")
    BpmProcessInstancePageItemRespVO convert(BpmProcessInstanceExtDO bean);

    List<BpmProcessInstancePageItemRespVO.Task> convertList2(List<Task> tasks);

    default BpmProcessInstanceRespVO convert2(HistoricProcessInstance processInstance, BpmProcessInstanceExtDO processInstanceExt,
                                              ProcessDefinition processDefinition, BpmProcessDefinitionExtDO processDefinitionExt,
                                              String bpmnXml, UserVO startUser, DeptSmallDto dept) {
        BpmProcessInstanceRespVO respVO = convert2(processInstance);
        copyTo(processInstanceExt, respVO);
        // definition
        respVO.setProcessDefinition(convert2(processDefinition));
        copyTo(processDefinitionExt, respVO.getProcessDefinition());
        respVO.getProcessDefinition().setBpmnXml(bpmnXml);
        // user
        if (startUser != null) {
            respVO.setStartUser(convert2(startUser));
            if (dept != null) {
                respVO.getStartUser().setDeptName(dept.getName());
            }
        }
        return respVO;
    }

    BpmProcessInstanceRespVO convert2(HistoricProcessInstance bean);

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessInstanceExtDO from, @MappingTarget BpmProcessInstanceRespVO to);

    BpmProcessInstanceRespVO.ProcessDefinition convert2(ProcessDefinition bean);

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionExtDO from, @MappingTarget BpmProcessInstanceRespVO.ProcessDefinition to);

    BpmProcessInstanceRespVO.User convert2(UserVO bean);

    default BpmProcessInstanceResultEvent convert(Object source, HistoricProcessInstance instance, Integer result) {
        BpmProcessInstanceResultEvent event = new BpmProcessInstanceResultEvent(source);
        event.setId(instance.getId());
        event.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        event.setBusinessKey(instance.getBusinessKey());
        event.setResult(result);
        return event;
    }

    default BpmProcessInstanceResultEvent convert(Object source, ProcessInstance instance, Integer result) {
        BpmProcessInstanceResultEvent event = new BpmProcessInstanceResultEvent(source);
        event.setId(instance.getId());
        event.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        event.setBusinessKey(instance.getBusinessKey());
        event.setResult(result);
        return event;
    }

    default BpmMessageSendWhenProcessInstanceApproveReqDTO convert2ApprovedReq(ProcessInstance instance) {
        BpmMessageSendWhenProcessInstanceApproveReqDTO dto = new BpmMessageSendWhenProcessInstanceApproveReqDTO();
        dto.setStartUserId(NumberUtil.parseLong(instance.getStartUserId()));
        dto.setProcessInstanceId(instance.getId());
        dto.setProcessInstanceName(instance.getName());
        return dto;
    }

    default BpmMessageSendWhenProcessInstanceRejectReqDTO convert2RejectReq(ProcessInstance instance, String reason) {
        BpmMessageSendWhenProcessInstanceRejectReqDTO dto = new BpmMessageSendWhenProcessInstanceRejectReqDTO();
        dto.setProcessInstanceName(instance.getName());
        dto.setProcessInstanceId(instance.getId());
        dto.setReason(reason);
        dto.setStartUserId(NumberUtil.parseLong(instance.getStartUserId()));
        return dto;
    }
}
