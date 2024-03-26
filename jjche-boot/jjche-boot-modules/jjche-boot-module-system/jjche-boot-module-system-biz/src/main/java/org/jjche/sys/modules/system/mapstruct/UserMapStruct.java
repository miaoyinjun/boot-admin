package org.jjche.sys.modules.system.mapstruct;

import org.jjche.common.vo.UserSampleVO;
import org.jjche.common.vo.UserVO;
import org.jjche.core.base.BaseMapStruct;
import org.jjche.sys.modules.system.dto.UserDTO;
import org.jjche.sys.modules.system.domain.UserDO;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * <p>UserMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Mapper(componentModel = "spring")
public interface UserMapStruct extends BaseMapStruct<UserDO, UserDTO, UserVO> {
    List<UserSampleVO> toSampleVO(Collection<UserDO> UserDOdooList);
}
