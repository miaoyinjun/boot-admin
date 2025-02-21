package org.jjche.sys.modules.mnt.mapstruct;

import org.jjche.core.base.BaseVoMapStruct;
import org.jjche.sys.modules.mnt.domain.DatabaseDO;
import org.jjche.sys.modules.mnt.dto.DatabaseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DatabaseMapStruct interface.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseMapStruct extends BaseVoMapStruct<DatabaseDO, DatabaseDTO> {

}
