package org.jjche.sys.modules.system.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.sys.modules.system.dto.DataPermissionRuleDTO;
import org.jjche.sys.modules.system.vo.DataPermissionRuleVO;
import org.jjche.sys.modules.system.domain.DataPermissionRuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 数据规则 转换类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-10-27
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionRuleMapStruct extends BaseMapStruct<DataPermissionRuleDO, DataPermissionRuleDTO, DataPermissionRuleVO> {
}
