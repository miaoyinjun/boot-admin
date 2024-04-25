package org.jjche.bpm.modules.definition.mapstruct;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.flowable.bpmn.model.UserTask;
import org.jjche.bpm.modules.definition.domain.BpmTaskAssignRuleDO;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleRespVO;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmTaskAssignRuleConvert {
    default List<BpmTaskAssignRuleRespVO> convertList(List<UserTask> tasks, List<BpmTaskAssignRuleDO> rules) {
        Map<String, BpmTaskAssignRuleDO> ruleMap = MapUtil.newHashMap();
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
