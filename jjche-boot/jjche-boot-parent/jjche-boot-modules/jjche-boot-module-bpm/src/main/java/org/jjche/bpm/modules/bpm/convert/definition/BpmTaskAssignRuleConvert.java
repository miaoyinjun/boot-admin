package org.jjche.bpm.modules.bpm.convert.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.runtime.ProcessInstance;
import org.jjche.bpm.modules.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import org.jjche.bpm.modules.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleRespVO;
import org.jjche.bpm.modules.bpm.controller.admin.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import org.jjche.bpm.modules.bpm.dal.dataobject.definition.BpmTaskAssignRuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface BpmTaskAssignRuleConvert {
    BpmTaskAssignRuleConvert INSTANCE = Mappers.getMapper(BpmTaskAssignRuleConvert.class);

    default List<BpmTaskAssignRuleRespVO> convertList(List<UserTask> tasks, List<BpmTaskAssignRuleDO> rules) {
        Map<String, BpmTaskAssignRuleDO> ruleMap = MapUtil.empty();
        ruleMap = CollUtil.toMap(rules, ruleMap, BpmTaskAssignRuleDO::getTaskDefinitionKey);
        List<BpmTaskAssignRuleRespVO> result = CollUtil.newArrayList();
        // 以 UserTask 为主维度，原因是：流程图编辑后，一些规则实际就没用了。
        for (UserTask task : tasks) {
            BpmTaskAssignRuleRespVO respVO = convert(ruleMap.get(task.getId()));
            if (respVO == null) {
                respVO = new BpmTaskAssignRuleRespVO();
                respVO.setTaskDefinitionKey(task.getId());
            }
            respVO.setTaskDefinitionName(task.getName());
            result.add(respVO);
        }
        return result;
    }

    BpmTaskAssignRuleRespVO convert(BpmTaskAssignRuleDO bean);

    BpmTaskAssignRuleDO convert(BpmTaskAssignRuleCreateReqVO bean);

    BpmTaskAssignRuleDO convert(BpmTaskAssignRuleUpdateReqVO bean);

    List<BpmTaskAssignRuleDO> convertList2(List<BpmTaskAssignRuleRespVO> list);
}
