package org.jjche.bpm.modules.task.mapstruct;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.jjche.bpm.modules.task.vo.task.BpmTaskDonePageItemRespVO;
import org.jjche.common.dto.DeptSmallDTO;
import org.jjche.common.dto.UserVO;
import org.jjche.bpm.modules.task.domain.BpmTaskExtDO;
import org.jjche.bpm.modules.task.vo.task.BpmTaskRespVO;
import org.jjche.bpm.modules.task.vo.task.BpmTaskTodoPageItemRespVO;
import org.jjche.bpm.modules.message.dto.BpmMessageSendWhenTaskCreatedReqDTO;
import org.mapstruct.*;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bpm 任务 Convert
 *
 * @author 芋道源码
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmTaskConvert {
    /**
     * 复制对象
     *
     * @param source 源 要复制的对象
     * @param target 目标 复制到此对象
     * @param <T>
     *
     * @return
     */
    public static <T> T copy(Object source, Class<T> target) {
        if (source == null || target == null) {
            return null;
        }
        try {
            T newInstance = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, newInstance);
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T, K> List<K> copyList(List<T> source, Class<K> target) {
        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> copy(e, target)).collect(Collectors.toList());
    }

    default List<BpmTaskTodoPageItemRespVO> convertList1(List<Task> tasks,
                                                         Map<String, ProcessInstance> processInstanceMap, Map<Long, UserVO> userMap) {
        List<BpmTaskTodoPageItemRespVO> result = CollUtil.newArrayList();
        for (Task task : tasks) {
            BpmTaskTodoPageItemRespVO respVO = convert1(task);
            ProcessInstance processInstance = processInstanceMap.get(task.getProcessInstanceId());
            if (processInstance != null) {
                UserVO startUser = userMap.get(NumberUtil.parseLong(processInstance.getStartUserId()));
                respVO.setProcessInstance(convert(processInstance, startUser));
            }
            result.add(respVO);
        }
        return result;
    }

    @Mapping(source = "suspended", target = "suspensionState", qualifiedByName = "convertSuspendedToSuspensionState")
//    @Mapping(target = "claimTime", expression = "java(bean.getClaimTime()==null?null: LocalDateTime.ofInstant(bean.getClaimTime().toInstant(),ZoneId.systemDefault()))")
//    @Mapping(target = "createTime", expression = "java(bean.getCreateTime()==null?null:LocalDateTime.ofInstant(bean.getCreateTime().toInstant(),ZoneId.systemDefault()))")
    BpmTaskTodoPageItemRespVO convert1(Task bean);

    @Named("convertSuspendedToSuspensionState")
    default Integer convertSuspendedToSuspensionState(boolean suspended) {
        return suspended ? SuspensionState.SUSPENDED.getStateCode() : SuspensionState.ACTIVE.getStateCode();
    }

    default List<BpmTaskDonePageItemRespVO> convertList2(List<HistoricTaskInstance> tasks,
                                                         Map<String, BpmTaskExtDO> bpmTaskExtDOMap, Map<String, HistoricProcessInstance> historicProcessInstanceMap,
                                                         Map<Long, UserVO> userMap) {
        List<BpmTaskDonePageItemRespVO> result = CollUtil.newArrayList();
        for (HistoricTaskInstance task : tasks) {
            BpmTaskDonePageItemRespVO respVO = convert2(task);
            BpmTaskExtDO taskExtDO = bpmTaskExtDOMap.get(task.getId());
            copyTo(taskExtDO, respVO);
            HistoricProcessInstance processInstance = historicProcessInstanceMap.get(task.getProcessInstanceId());
            if (processInstance != null) {
                UserVO startUser = userMap.get(NumberUtil.parseLong(processInstance.getStartUserId()));
                respVO.setProcessInstance(convert(processInstance, startUser));
            }
            result.add(respVO) ;
        }
        return result;
    }

    BpmTaskDonePageItemRespVO convert2(HistoricTaskInstance bean);

    @Mappings({@Mapping(source = "processInstance.id", target = "id"),
        @Mapping(source = "processInstance.name", target = "name"),
        @Mapping(source = "processInstance.startUserId", target = "startUserId"),
        @Mapping(source = "processInstance.processDefinitionId", target = "processDefinitionId"),
        @Mapping(source = "startUser.nickName", target = "startUserNickname")})
    BpmTaskTodoPageItemRespVO.ProcessInstance convert(ProcessInstance processInstance, UserVO startUser);

    default List<BpmTaskRespVO> convertList3(List<HistoricTaskInstance> tasks,
                                             Map<String, BpmTaskExtDO> bpmTaskExtDOMap, HistoricProcessInstance processInstance,
                                             Map<Long, UserVO> userMap) {
        List<BpmTaskRespVO> result = CollUtil.newArrayList();
        for (HistoricTaskInstance task : tasks) {
            BpmTaskRespVO respVO = convert3(task);
            BpmTaskExtDO taskExtDO = bpmTaskExtDOMap.get(task.getId());
            copyTo(taskExtDO, respVO);
            if (processInstance != null) {
                UserVO startUser = userMap.get(NumberUtil.parseLong(processInstance.getStartUserId()));
                respVO.setProcessInstance(convert(processInstance, startUser));
            }
            UserVO assignUser = userMap.get(NumberUtil.parseLong(task.getAssignee()));
            if (assignUser != null) {
                BpmTaskRespVO.User assigneeUser = new BpmTaskRespVO.User();
                assigneeUser.setId(assignUser.getId());
                assigneeUser.setNickName(assignUser.getNickName());
                DeptSmallDTO dept = assignUser.getDept();
                if (dept != null) {
                    assigneeUser.setDeptId(dept.getId());
                    assigneeUser.setDeptName(dept.getName());
                }
                respVO.setAssigneeUser(assigneeUser);
            }
            result.add(respVO);
        }
        return result;
    }

    @Mapping(source = "taskDefinitionKey", target = "definitionKey")
    BpmTaskRespVO convert3(HistoricTaskInstance bean);

    @Mapping(target = "id", ignore = true)
    void copyTo(BpmTaskExtDO from, @MappingTarget BpmTaskDonePageItemRespVO to);

    @Mappings({@Mapping(source = "processInstance.id", target = "id"),
        @Mapping(source = "processInstance.name", target = "name"),
        @Mapping(source = "processInstance.startUserId", target = "startUserId"),
        @Mapping(source = "processInstance.processDefinitionId", target = "processDefinitionId"),
        @Mapping(source = "startUser.nickName", target = "startUserNickname")})
    BpmTaskTodoPageItemRespVO.ProcessInstance convert(HistoricProcessInstance processInstance,
        UserVO startUser);

    default BpmTaskExtDO convert2TaskExt(Task task) {
        BpmTaskExtDO taskExtDO = new BpmTaskExtDO();
        taskExtDO.setTaskId(task.getId());
        taskExtDO.setAssigneeUserId(NumberUtil.parseLong(task.getAssignee()));
        taskExtDO.setName(task.getName());
        taskExtDO.setProcessDefinitionId(task.getProcessDefinitionId());
        taskExtDO.setProcessInstanceId(task.getProcessInstanceId());
        taskExtDO.setGmtCreate(DateUtil.date(task.getCreateTime()).toTimestamp());
        return taskExtDO;
    }

    default BpmMessageSendWhenTaskCreatedReqDTO convert(ProcessInstance processInstance, UserVO startUser,
                                                        Task task) {
        BpmMessageSendWhenTaskCreatedReqDTO reqDTO = new BpmMessageSendWhenTaskCreatedReqDTO();
        reqDTO.setProcessInstanceId(processInstance.getProcessInstanceId());
        reqDTO.setProcessInstanceName(processInstance.getName());
        reqDTO.setStartUserId(startUser.getId());
        reqDTO.setStartUserNickname(startUser.getNickName());
        reqDTO.setTaskId(task.getId());
        reqDTO.setTaskName(task.getName());
        reqDTO.setAssigneeUserId(NumberUtil.parseLong(task.getAssignee()));
        return reqDTO;
    }

}
