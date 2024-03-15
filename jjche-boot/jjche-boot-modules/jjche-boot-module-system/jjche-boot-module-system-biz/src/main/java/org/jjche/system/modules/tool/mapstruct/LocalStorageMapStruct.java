package org.jjche.system.modules.tool.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.system.modules.tool.api.dto.LocalStorageDTO;
import org.jjche.system.modules.tool.domain.LocalStorageDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>LocalStorageMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-09-05
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapStruct extends BaseVoMapStruct<LocalStorageDO, LocalStorageDTO> {

}
