package org.jjche.system.modules.mnt.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.system.modules.mnt.domain.AppDO;
import org.jjche.system.modules.mnt.dto.AppDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>AppMapStruct interface.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppMapStruct extends BaseMapStruct<AppDO, AppDTO, AppDTO> {

}
