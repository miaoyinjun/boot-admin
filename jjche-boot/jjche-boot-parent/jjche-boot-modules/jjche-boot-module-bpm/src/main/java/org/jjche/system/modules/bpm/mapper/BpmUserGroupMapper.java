package org.jjche.system.modules.bpm.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmUserGroupDO;

import java.util.List;

/**
 * 用户组 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmUserGroupMapper extends MyBaseMapper<BpmUserGroupDO> {

    default List<BpmUserGroupDO> selectListByStatus(Boolean status) {
        LambdaQueryWrapper<BpmUserGroupDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BpmUserGroupDO::getStatus, status);
        return selectList(wrapper);
    }

}
