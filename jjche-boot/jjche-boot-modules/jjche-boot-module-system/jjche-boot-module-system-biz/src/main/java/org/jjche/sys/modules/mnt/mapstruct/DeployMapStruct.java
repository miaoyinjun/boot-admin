package org.jjche.sys.modules.mnt.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.sys.modules.mnt.domain.DeployDO;
import org.jjche.sys.modules.mnt.api.dto.DeployDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DeployMapStruct interface.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Mapper(componentModel = "spring", uses = {AppMapStruct.class, ServerDeployMapStruct.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployMapStruct extends BaseVoMapStruct<DeployDO, DeployDTO> {

}
