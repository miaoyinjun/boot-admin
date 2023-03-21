package org.jjche.system.modules.system.mapstruct;

import org.jjche.common.dto.RoleSmallDto;
import org.jjche.system.modules.system.domain.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

/**
 * <p>RoleSmallMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-5-23
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleSmallMapStruct {
    List<RoleSmallDto> toVO(Collection<RoleDO> dooList);
}
