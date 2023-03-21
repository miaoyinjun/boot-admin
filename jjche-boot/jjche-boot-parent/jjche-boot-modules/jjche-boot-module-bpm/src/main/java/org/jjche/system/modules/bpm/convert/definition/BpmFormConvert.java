package org.jjche.system.modules.bpm.convert.definition;

import org.jjche.common.param.MyPage;
import org.jjche.system.modules.bpm.rest.admin.definition.dto.form.BpmFormDTO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.form.BpmFormRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.form.BpmFormSimpleRespVO;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmFormDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 动态表单 Convert
 *
 * @author 芋艿
 */
@Mapper
public interface BpmFormConvert  {

    BpmFormConvert INSTANCE = Mappers.getMapper(BpmFormConvert.class);

    BpmFormDO convert(BpmFormDTO bean);

    BpmFormRespVO convert(BpmFormDO bean);

    List<BpmFormSimpleRespVO> convertList2(List<BpmFormDO> list);

    MyPage<BpmFormRespVO> convertPage(MyPage<BpmFormDO> page);

}
