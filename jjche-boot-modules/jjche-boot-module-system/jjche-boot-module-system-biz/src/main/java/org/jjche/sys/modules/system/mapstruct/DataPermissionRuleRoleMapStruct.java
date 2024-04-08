package org.jjche.sys.modules.system.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.sys.modules.system.dto.DataPermissionRuleRoleDTO;
import org.jjche.sys.modules.system.vo.DataPermissionRuleRoleVO;
import org.jjche.sys.modules.system.domain.DataPermissionRuleRoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 数据规则权限 转换类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-01
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionRuleRoleMapStruct extends BaseMapStruct<DataPermissionRuleRoleDO, DataPermissionRuleRoleDTO, DataPermissionRuleRoleVO> {
}
