package org.jjche.system.modules.system.mapstruct;

import org.jjche.common.dto.DeptSmallDto;
import org.jjche.core.base.BaseMapStruct;
import org.jjche.system.modules.system.domain.DeptDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DeptSmallMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-25
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptSmallMapStruct extends BaseMapStruct<DeptDO, DeptSmallDto, DeptSmallDto> {

}
