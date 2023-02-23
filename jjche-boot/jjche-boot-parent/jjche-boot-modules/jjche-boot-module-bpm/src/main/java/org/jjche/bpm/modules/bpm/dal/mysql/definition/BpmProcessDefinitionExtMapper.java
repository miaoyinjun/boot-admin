package org.jjche.bpm.modules.bpm.dal.mysql.definition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.bpm.modules.bpm.dal.dataobject.definition.BpmFormDO;
import org.jjche.bpm.modules.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
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
