package org.jjche.sys.modules.system.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.sys.modules.system.dto.DataPermissionFieldDTO;
import org.jjche.sys.modules.system.vo.DataPermissionFieldVO;
import org.jjche.sys.modules.system.domain.DataPermissionFieldDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 数据字段映射
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-18
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionFieldMapStruct extends BaseMapStruct<DataPermissionFieldDO, DataPermissionFieldDTO, DataPermissionFieldVO> {

}
