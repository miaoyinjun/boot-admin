package org.jjche.sys.modules.system.mapstruct;

import org.jjche.sys.modules.system.dto.RoleDTO;
import org.jjche.sys.modules.system.domain.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

/**
 * <p>RoleMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapStruct  {
    List<RoleDTO> toVO(Collection<RoleDO> dooList);
    RoleDTO toVO(RoleDO doo);
}
