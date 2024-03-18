package org.jjche.cloud.modules.system.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.system.modules.system.api.dto.UserDTO;
import org.jjche.system.modules.system.domain.UserDO;
import org.jjche.system.modules.user.api.vo.UserExtVO;
import org.mapstruct.Mapper;

/**
 * <p>
 * 用户扩展服务
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-08
 */
@Mapper(componentModel = "spring")
public interface UserExtMapStruct extends BaseMapStruct<UserDO, UserDTO, UserExtVO> {
}
