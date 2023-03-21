package org.jjche.system.modules.bpm.convert.oa;
import org.jjche.system.modules.bpm.rest.admin.oa.vo.BpmOALeaveCreateReqVO;
import org.jjche.system.modules.bpm.rest.admin.oa.vo.BpmOALeaveRespVO;
import org.jjche.system.modules.bpm.dal.dataobject.oa.BpmOALeaveDO;
import org.jjche.common.param.MyPage;
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

    MyPage<BpmOALeaveRespVO> convertPage(MyPage<BpmOALeaveDO> page);

}
