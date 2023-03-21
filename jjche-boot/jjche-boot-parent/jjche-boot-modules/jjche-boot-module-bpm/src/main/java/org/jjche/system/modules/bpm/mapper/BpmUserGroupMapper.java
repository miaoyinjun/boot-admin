package org.jjche.system.modules.bpm.mapper;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupPageReqVO;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmUserGroupDO;
import org.jjche.common.param.MyPage;
import org.jjche.common.util.StrUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户组 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmUserGroupMapper extends BaseMapper<BpmUserGroupDO> {

    default MyPage<BpmUserGroupDO> selectPage(BpmUserGroupPageReqVO reqVO) {

        LambdaQueryWrapper<BpmUserGroupDO> wrapper = Wrappers.lambdaQuery();
        String name = reqVO.getName();
        if (StrUtil.isNotBlank(name)) {
            wrapper.like(BpmUserGroupDO::getName, name);
        }
        Integer status = reqVO.getStatus();
        if (status != null) {
            wrapper.eq(BpmUserGroupDO::getStatus, status);
        }
        LocalDateTime[] createTime = reqVO.getCreateTime();
        Object val1 = ArrayUtil.get(createTime, 0);
        Object val2 = ArrayUtil.get(createTime, 1);
        if (createTime != null && createTime.length == 2) {
            wrapper.between(BpmUserGroupDO::getGmtCreate, val1, val2);
        }
        return selectPage(reqVO, wrapper);
    }

    default List<BpmUserGroupDO> selectListByStatus(Integer status) {
        LambdaQueryWrapper<BpmUserGroupDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BpmUserGroupDO::getStatus, status);
        return selectList(wrapper);
    }

}
