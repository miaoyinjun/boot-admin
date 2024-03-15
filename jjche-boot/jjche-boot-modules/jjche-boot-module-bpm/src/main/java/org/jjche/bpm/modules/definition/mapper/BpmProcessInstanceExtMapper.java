package org.jjche.bpm.modules.definition.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.jjche.bpm.modules.task.domain.BpmProcessInstanceExtDO;
import org.jjche.common.param.MyPage;
import org.jjche.common.util.StrUtil;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.bpm.modules.task.api.vo.instance.BpmProcessInstanceMyPageReqVO;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface BpmProcessInstanceExtMapper extends MyBaseMapper<BpmProcessInstanceExtDO> {

    default MyPage<BpmProcessInstanceExtDO> selectPage(Long userId, BpmProcessInstanceMyPageReqVO reqVO) {
        String name = reqVO.getName();
        String processDefinitionId = reqVO.getProcessDefinitionId();
        String category = reqVO.getCategory();
        Integer status = reqVO.getStatus();
        Integer result = reqVO.getResult();

        List<Timestamp> gmtCreate = reqVO.getGmtCreate();

        LambdaQueryWrapper<BpmProcessInstanceExtDO> wrapper = Wrappers.lambdaQuery();
        if (userId != null) {
            wrapper.eq(BpmProcessInstanceExtDO::getStartUserId, userId);
        }
        if (StrUtil.isNotBlank(name)) {
            wrapper.eq(BpmProcessInstanceExtDO::getName, name);
        }
        if (StrUtil.isNotBlank(processDefinitionId)) {
            wrapper.eq(BpmProcessInstanceExtDO::getProcessDefinitionId, processDefinitionId);
        }
        if (StrUtil.isNotBlank(processDefinitionId)) {
            wrapper.eq(BpmProcessInstanceExtDO::getCategory, category);
        }
        if (status != null) {
            wrapper.eq(BpmProcessInstanceExtDO::getStatus, status);
        }
        if (result != null) {
            wrapper.eq(BpmProcessInstanceExtDO::getResult, result);
        }

        if (CollUtil.isNotEmpty(gmtCreate)) {
            wrapper.between(BpmProcessInstanceExtDO::getGmtCreate, CollUtil.getFirst(gmtCreate), CollUtil.getLast(gmtCreate));
        }
        return selectPage(reqVO, wrapper);
    }

    default BpmProcessInstanceExtDO selectByProcessInstanceId(String processInstanceId) {
        LambdaQueryWrapper<BpmProcessInstanceExtDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BpmProcessInstanceExtDO::getProcessInstanceId, processInstanceId);
        return selectOne(wrapper);
    }

    default void updateByProcessInstanceId(BpmProcessInstanceExtDO updateObj) {
        update(updateObj, new LambdaQueryWrapper<BpmProcessInstanceExtDO>()
                .eq(BpmProcessInstanceExtDO::getProcessInstanceId, updateObj.getProcessInstanceId()));
    }

}
