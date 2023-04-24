package org.jjche.system.modules.bpm.mapper;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.common.param.MyPage;
import org.jjche.common.util.StrUtil;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.system.modules.bpm.dal.dataobject.oa.BpmOALeaveDO;
import org.jjche.system.modules.bpm.rest.admin.oa.vo.BpmOALeavePageReqVO;

import java.sql.Timestamp;

/**
 * 请假申请 Mapper
 *
 * @author jason
 * @author 芋道源码
 */
@Mapper
public interface BpmOALeaveMapper extends MyBaseMapper<BpmOALeaveDO> {

    default MyPage<BpmOALeaveDO> selectPage(Long userId, BpmOALeavePageReqVO reqVO) {
        Integer result = reqVO.getResult();
        Integer type = reqVO.getType();
        String reason = reqVO.getReason();
        Timestamp[] createTime = reqVO.getCreateTime();

        LambdaQueryWrapper<BpmOALeaveDO> wrapper = Wrappers.lambdaQuery();
        if (userId != null) {
            wrapper.eq(BpmOALeaveDO::getUserId, userId);
        }
        if (result != null) {
            wrapper.eq(BpmOALeaveDO::getResult, result);
        }
        if (type != null) {
            wrapper.eq(BpmOALeaveDO::getType, type);
        }
        if (StrUtil.isNotBlank(reason)) {
            wrapper.like(BpmOALeaveDO::getReason, reason);
        }
        if (createTime != null && createTime.length == 2) {
            Object val1 = ArrayUtil.get(createTime, 0);
            Object val2 = ArrayUtil.get(createTime, 1);
            wrapper.between(BpmOALeaveDO::getGmtCreate, val1, val2);
        }
        return selectPage(reqVO, wrapper);
    }

}
