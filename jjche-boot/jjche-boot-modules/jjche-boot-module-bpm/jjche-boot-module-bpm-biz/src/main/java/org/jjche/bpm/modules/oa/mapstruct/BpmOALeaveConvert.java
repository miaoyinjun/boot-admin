package org.jjche.bpm.modules.oa.mapstruct;

import org.jjche.bpm.modules.oa.domain.BpmOALeaveDO;
import org.jjche.bpm.modules.oa.api.vo.BpmOALeaveCreateReqDTO;
import org.jjche.bpm.modules.oa.api.vo.BpmOALeaveRespVO;
import org.jjche.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 请假申请 Convert
 *
 * @author 芋艿
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmOALeaveConvert extends BaseMapStruct<BpmOALeaveDO, BpmOALeaveCreateReqDTO, BpmOALeaveRespVO> {
}
