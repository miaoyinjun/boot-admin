package org.jjche.sys.modules.mnt.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.sys.modules.mnt.domain.ServerDeployDO;
import org.jjche.sys.modules.mnt.dto.ServerDeployDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>ServerDeployMapStruct interface.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServerDeployMapStruct extends BaseVoMapStruct<ServerDeployDO, ServerDeployDTO> {

}
