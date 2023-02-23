package org.jjche.bpm.modules.bpm.convert.oa;
import cn.hutool.db.PageResult;
import org.jjche.bpm.modules.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import org.jjche.bpm.modules.bpm.controller.admin.oa.vo.BpmOALeaveRespVO;
import org.jjche.bpm.modules.bpm.dal.dataobject.oa.BpmOALeaveDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 请假申请 Convert
 *
 * @author 芋艿
 */
@Mapper
public interface BpmOALeaveConvert {

    BpmOALeaveConvert INSTANCE = Mappers.getMapper(BpmOALeaveConvert.class);

    BpmOALeaveDO convert(BpmOALeaveCreateReqVO bean);

    BpmOALeaveRespVO convert(BpmOALeaveDO bean);

    List<BpmOALeaveRespVO> convertList(List<BpmOALeaveDO> list);

    PageResult<BpmOALeaveRespVO> convertPage(PageResult<BpmOALeaveDO> page);

}
