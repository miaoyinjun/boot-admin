package org.jjche.system.modules.bpm.framework.flowable.core.behavior.script.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.jjche.system.modules.bpm.enums.definition.BpmTaskRuleScriptEnum;
import org.jjche.system.modules.bpm.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import org.jjche.system.modules.bpm.service.task.BpmProcessInstanceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 分配给发起人审批的 Script 实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmTaskAssignStartUserScript implements BpmTaskAssignScript {

    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService bpmProcessInstanceService;

    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        ProcessInstance processInstance = bpmProcessInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtil.parseLong(processInstance.getStartUserId());
        return CollUtil.newHashSet(startUserId);
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.START_USER;
    }

}
