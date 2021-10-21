package com.boot.admin.system.modules.mnt.mapstruct;

import com.boot.admin.system.modules.mnt.domain.ServerDeployDO;
import com.boot.admin.system.modules.mnt.dto.ServerDeployDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>ServerDeployMapStruct interface.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerDeployMapStruct extends BaseMapStruct<ServerDeployDO, ServerDeployDTO, ServerDeployDTO> {

}
