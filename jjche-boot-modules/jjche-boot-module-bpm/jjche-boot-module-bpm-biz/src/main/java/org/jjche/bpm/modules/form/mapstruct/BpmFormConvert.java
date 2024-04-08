package org.jjche.bpm.modules.form.mapstruct;

import org.jjche.bpm.modules.form.domain.BpmFormDO;
import org.jjche.bpm.modules.form.dto.BpmFormDTO;
import org.jjche.bpm.modules.form.vo.BpmFormDetailRespVO;
import org.jjche.bpm.modules.form.vo.BpmFormRespVO;
import org.jjche.bpm.modules.form.vo.BpmFormSimpleRespVO;
import org.jjche.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 动态表单 Convert
 *
 * @author 芋艿
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BpmFormConvert extends BaseMapStruct<BpmFormDO, BpmFormDTO, BpmFormRespVO> {
    @Named("two")
    BpmFormDetailRespVO toDetailVO(BpmFormDO beand);

    List<BpmFormSimpleRespVO> toSimpleVO(List<BpmFormDO> list);
}
