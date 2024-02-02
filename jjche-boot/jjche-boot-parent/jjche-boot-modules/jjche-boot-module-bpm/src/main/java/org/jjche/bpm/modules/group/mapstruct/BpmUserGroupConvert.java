package org.jjche.bpm.modules.group.mapstruct;

import org.jjche.bpm.modules.group.domain.BpmUserGroupDO;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupCreateReqVO;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupRespVO;
import org.jjche.bpm.modules.group.api.vo.BpmUserGroupUpdateReqVO;
import org.jjche.core.base.BaseVoMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 用户组 Convert
 *
 * @author 芋道源码
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmUserGroupConvert extends BaseVoMapStruct<BpmUserGroupDO, BpmUserGroupRespVO> {
    BpmUserGroupDO convert(BpmUserGroupCreateReqVO bean);

    BpmUserGroupDO convert(BpmUserGroupUpdateReqVO bean);
}
