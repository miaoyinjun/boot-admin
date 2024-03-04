package org.jjche.system.modules.app.mapstruct;

import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.jjche.core.base.BaseMapStruct;
import org.jjche.system.modules.app.api.dto.SecurityAppKeyDTO;
import org.jjche.system.modules.app.api.vo.SecurityAppKeyVO;
import org.jjche.system.modules.app.domain.SecurityAppKeyDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * <p>
 * 应用密钥 转换类
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SecurityAppKeyMapStruct extends BaseMapStruct<SecurityAppKeyDO, SecurityAppKeyDTO, SecurityAppKeyVO> {
    List<SecurityAppKeyBasicVO> toBasicVO(List<SecurityAppKeyVO> vo);

    SecurityAppKeyBasicVO toBasicVO(SecurityAppKeyDO doo);
}