package org.jjche.system.modules.bpm.convert.definition;

import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupCreateReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.group.BpmUserGroupUpdateReqVO;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmUserGroupDO;
import org.jjche.common.param.MyPage;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户组 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmUserGroupConvert {

    BpmUserGroupConvert INSTANCE = Mappers.getMapper(BpmUserGroupConvert.class);

    BpmUserGroupDO convert(BpmUserGroupCreateReqVO bean);

    BpmUserGroupDO convert(BpmUserGroupUpdateReqVO bean);

    BpmUserGroupRespVO convert(BpmUserGroupDO bean);

    List<BpmUserGroupRespVO> convertList(List<BpmUserGroupDO> list);

    MyPage<BpmUserGroupRespVO> convertPage(MyPage<BpmUserGroupDO> page);

    @Named("convertList2")
    List<BpmUserGroupRespVO> convertList2(List<BpmUserGroupDO> list);

}
