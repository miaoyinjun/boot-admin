package org.jjche.bpm.modules.oa.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.definition.api.BpmProcessInstanceApi;
import org.jjche.bpm.modules.definition.api.dto.BpmProcessInstanceCreateReqDTO;
import org.jjche.bpm.modules.oa.domain.BpmOALeaveDO;
import org.jjche.bpm.modules.oa.mapper.BpmOALeaveMapper;
import org.jjche.bpm.modules.oa.mapstruct.BpmOALeaveConvert;
import org.jjche.bpm.modules.oa.vo.BpmOALeaveCreateReqDTO;
import org.jjche.bpm.modules.oa.vo.BpmOALeavePageReqVO;
import org.jjche.bpm.modules.task.enums.BpmProcessInstanceResultEnum;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

/**
 * OA 请假申请 Service 实现类
 *
 * @author jason
 * @author 芋道源码
 */
@Service
@Validated
@RequiredArgsConstructor
public class BpmOALeaveService extends MyServiceImpl<BpmOALeaveMapper, BpmOALeaveDO> {

    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "oa_leave";

    private final BpmProcessInstanceApi processInstanceApi;
    private final BpmOALeaveConvert leaveConvert;

    /**
     * 创建请假申请
     *
     * @param userId 用户编号
     * @param createReqVO 创建信息
     * @return 编号
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createLeave(Long userId, BpmOALeaveCreateReqDTO createReqVO) {
        // 插入 OA 请假单
        long day = LocalDateTimeUtil.between(createReqVO.getStartTime().toLocalDateTime(), createReqVO.getEndTime().toLocalDateTime()).toDays();
        BpmOALeaveDO leave = leaveConvert.toDO(createReqVO);
        leave.setUserId(userId);
        leave.setDay(day);
        leave.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        this.save(leave);

        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("day", day);
        BpmProcessInstanceCreateReqDTO bpmProcessInstanceCreateReqDTO = new BpmProcessInstanceCreateReqDTO();
        bpmProcessInstanceCreateReqDTO.setProcessDefinitionKey(PROCESS_KEY);
        bpmProcessInstanceCreateReqDTO.setVariables(processInstanceVariables);
        bpmProcessInstanceCreateReqDTO.setBusinessKey(String.valueOf(leave.getId()));
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                bpmProcessInstanceCreateReqDTO);

        // 将工作流的编号，更新到 OA 请假单中
        BpmOALeaveDO bpmOALeaveDO = new BpmOALeaveDO();
        bpmOALeaveDO.setId(leave.getId());
        bpmOALeaveDO.setProcessInstanceId(processInstanceId);
        this.updateById(bpmOALeaveDO);
        return leave.getId();
    }

    /**
     * 更新请假申请的状态
     *
     * @param id 编号
     * @param result 结果
     */
    public void updateLeaveResult(Long id, Integer result) {
        validateLeaveExists(id);
        BpmOALeaveDO bpmOALeaveDO = new BpmOALeaveDO();
        bpmOALeaveDO.setId(id);
        bpmOALeaveDO.setResult(result);
        this.updateById(bpmOALeaveDO);
    }

    private void validateLeaveExists(Long id) {
        if (this.getById(id) == null) {
            throw new BusinessException("请假申请不存在");
        }
    }

    /**
     * 获得请假申请
     *
     * @param id 编号
     * @return 请假申请
     */
    public BpmOALeaveDO getLeave(Long id) {
        return this.getById(id);
    }

    /**
     * 获得请假申请分页
     *
     * @param userId 用户编号
     * @param pageReqVO 分页查询
     * @return 请假申请分页
     */
    public MyPage<BpmOALeaveDO> getLeavePage(Long userId, BpmOALeavePageReqVO pageReqVO) {
        return this.baseMapper.selectPage(userId, pageReqVO);
    }

}
