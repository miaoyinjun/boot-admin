package org.jjche.bpm.modules.oa.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.bpm.modules.oa.domain.BpmOALeaveDO;
import org.jjche.bpm.modules.oa.api.vo.BpmOALeavePageReqVO;
import org.jjche.common.param.MyPage;
import org.jjche.common.util.StrUtil;
import org.jjche.mybatis.base.MyBaseMapper;

import java.sql.Timestamp;
import java.util.List;

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
        List<Timestamp> gmtCreate = reqVO.getGmtCreate();

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
        if (CollUtil.isNotEmpty(gmtCreate)) {
            wrapper.between(BpmOALeaveDO::getGmtCreate, CollUtil.getFirst(gmtCreate), CollUtil.getLast(gmtCreate));
        }
        return selectPage(reqVO, wrapper);
    }

}
