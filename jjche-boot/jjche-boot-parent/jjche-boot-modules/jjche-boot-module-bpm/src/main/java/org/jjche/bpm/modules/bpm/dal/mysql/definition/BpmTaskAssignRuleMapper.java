package org.jjche.bpm.modules.bpm.dal.mysql.definition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.bpm.modules.bpm.dal.dataobject.definition.BpmTaskAssignRuleDO;
import org.jjche.common.util.StrUtil;
import org.jjche.mybatis.base.MyBaseMapper;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface BpmTaskAssignRuleMapper extends MyBaseMapper<BpmTaskAssignRuleDO> {

    default List<BpmTaskAssignRuleDO> selectListByProcessDefinitionId(String processDefinitionId, @Nullable String taskDefinitionKey) {

        LambdaQueryWrapper<BpmTaskAssignRuleDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BpmTaskAssignRuleDO::getProcessDefinitionId, processDefinitionId);
        if (StrUtil.isNotBlank(taskDefinitionKey)) {
            wrapper.eq(BpmTaskAssignRuleDO::getTaskDefinitionKey, taskDefinitionKey);
        }
        return selectList(wrapper);
    }

    default List<BpmTaskAssignRuleDO> selectListByModelId(String modelId) {
        LambdaQueryWrapper<BpmTaskAssignRuleDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BpmTaskAssignRuleDO::getModelId, modelId);
        wrapper.eq(BpmTaskAssignRuleDO::getProcessDefinitionId, BpmTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL);
        return selectList(wrapper);
    }

    default BpmTaskAssignRuleDO selectListByModelIdAndTaskDefinitionKey(String modelId, String taskDefinitionKey) {
        LambdaQueryWrapper<BpmTaskAssignRuleDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BpmTaskAssignRuleDO::getModelId, modelId);
        wrapper.eq(BpmTaskAssignRuleDO::getProcessDefinitionId, BpmTaskAssignRuleDO.PROCESS_DEFINITION_ID_NULL);
        wrapper.eq(BpmTaskAssignRuleDO::getTaskDefinitionKey, taskDefinitionKey);
        return selectOne(wrapper);
    }
}
