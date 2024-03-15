package org.jjche.bpm.modules.definition.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.bpm.modules.definition.domain.BpmProcessDefinitionExtDO;
import org.jjche.mybatis.base.MyBaseMapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface BpmProcessDefinitionExtMapper extends MyBaseMapper<BpmProcessDefinitionExtDO> {

    default List<BpmProcessDefinitionExtDO> selectListByProcessDefinitionIds(Collection<String> processDefinitionIds) {
        LambdaQueryWrapper<BpmProcessDefinitionExtDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(BpmProcessDefinitionExtDO::getProcessDefinitionId, processDefinitionIds);
        return selectList(wrapper);
    }

    default BpmProcessDefinitionExtDO selectByProcessDefinitionId(String processDefinitionId) {
        LambdaQueryWrapper<BpmProcessDefinitionExtDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(BpmProcessDefinitionExtDO::getProcessDefinitionId, processDefinitionId);
        return selectOne(wrapper);
    }

}
