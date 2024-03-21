package org.jjche.sys.modules.system.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.sys.modules.system.api.dto.DataPermissionFieldRoleDTO;
import org.jjche.sys.modules.system.api.vo.DataPermissionFieldRoleVO;
import org.jjche.sys.modules.system.domain.DataPermissionFieldRoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 数据字段角色 转换类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-04
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionFieldRoleMapStruct extends BaseMapStruct<DataPermissionFieldRoleDO, DataPermissionFieldRoleDTO, DataPermissionFieldRoleVO> {
}
