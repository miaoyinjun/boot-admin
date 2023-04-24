package org.jjche.system.modules.bpm.service.oa;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.jjche.system.modules.bpm.api.BpmProcessInstanceApi;
import org.jjche.system.modules.bpm.api.dto.BpmProcessInstanceCreateReqDTO;
import org.jjche.system.modules.bpm.rest.admin.oa.vo.BpmOALeaveCreateReqVO;
import org.jjche.system.modules.bpm.rest.admin.oa.vo.BpmOALeavePageReqVO;
import org.jjche.system.modules.bpm.convert.oa.BpmOALeaveConvert;
import org.jjche.system.modules.bpm.dal.dataobject.oa.BpmOALeaveDO;
import org.jjche.system.modules.bpm.mapper.BpmOALeaveMapper;
import org.jjche.system.modules.bpm.enums.task.BpmProcessInstanceResultEnum;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.param.MyPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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
public class BpmOALeaveServiceImpl implements BpmOALeaveService {

    /**
     * OA 请假对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "oa_leave";

    @Resource
    private BpmOALeaveMapper leaveMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLeave(Long userId, BpmOALeaveCreateReqVO createReqVO) {
        // 插入 OA 请假单
        long day = LocalDateTimeUtil.between(createReqVO.getStartTime().toLocalDateTime(), createReqVO.getEndTime().toLocalDateTime()).toDays();
        BpmOALeaveDO leave = BpmOALeaveConvert.INSTANCE.convert(createReqVO);
        leave.setUserId(userId);
        leave.setDay(day);
        leave.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        leaveMapper.insert(leave);

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
        leaveMapper.updateById(bpmOALeaveDO);
        return leave.getId();
    }

    @Override
    public void updateLeaveResult(Long id, Integer result) {
        validateLeaveExists(id);
        BpmOALeaveDO bpmOALeaveDO = new BpmOALeaveDO();
        bpmOALeaveDO.setId(id);
        bpmOALeaveDO.setResult(result);
        leaveMapper.updateById(bpmOALeaveDO);
    }

    private void validateLeaveExists(Long id) {
        if (leaveMapper.selectById(id) == null) {
            throw new BusinessException("请假申请不存在");
        }
    }

    @Override
    public BpmOALeaveDO getLeave(Long id) {
        return leaveMapper.selectById(id);
    }

    @Override
    public MyPage<BpmOALeaveDO> getLeavePage(Long userId, BpmOALeavePageReqVO pageReqVO) {
        return leaveMapper.selectPage(userId, pageReqVO);
    }

}
