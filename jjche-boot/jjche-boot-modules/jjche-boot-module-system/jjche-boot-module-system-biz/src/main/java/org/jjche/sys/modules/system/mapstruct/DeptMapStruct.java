package org.jjche.sys.modules.system.mapstruct;

import org.jjche.common.dto.DeptSmallDTO;
import org.jjche.sys.modules.system.api.dto.DeptDTO;
import org.jjche.sys.modules.system.domain.DeptDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

/**
 * <p>DeptMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-25
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapStruct {
    List<DeptDTO> toVO(Collection<DeptDO> dooList);
    DeptDTO toVO(DeptDO doo);
    List<DeptSmallDTO> toSmallVO(Collection<DeptDO> dooList);
    DeptSmallDTO toSmallVO(DeptDO doo);
}
