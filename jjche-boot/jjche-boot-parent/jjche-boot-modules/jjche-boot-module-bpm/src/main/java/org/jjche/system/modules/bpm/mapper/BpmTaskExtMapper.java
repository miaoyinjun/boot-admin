package org.jjche.system.modules.bpm.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.system.modules.bpm.dal.dataobject.task.BpmTaskExtDO;

import java.util.Collection;
import java.util.List;

@Mapper
public interface BpmTaskExtMapper extends MyBaseMapper<BpmTaskExtDO> {

    default void updateByTaskId(BpmTaskExtDO entity) {
        update(entity, new LambdaQueryWrapper<BpmTaskExtDO>().eq(BpmTaskExtDO::getTaskId, entity.getTaskId()));
    }

    default List<BpmTaskExtDO> selectListByTaskIds(Collection<String> taskIds) {
        LambdaQueryWrapper<BpmTaskExtDO> wrapper = Wrappers.lambdaQuery();
            wrapper.in(BpmTaskExtDO::getTaskId, taskIds);
        return selectList(wrapper);
    }

    default BpmTaskExtDO selectByTaskId(String taskId) {
        LambdaQueryWrapper<BpmTaskExtDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BpmTaskExtDO::getTaskId, taskId);
        return selectOne(wrapper);
    }

}
