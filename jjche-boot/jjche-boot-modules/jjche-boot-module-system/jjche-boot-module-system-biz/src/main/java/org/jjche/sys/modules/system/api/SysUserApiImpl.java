package org.jjche.sys.modules.system.api;

import lombok.RequiredArgsConstructor;
import org.jjche.sys.modules.system.service.UserService;
import org.jjche.sys.modules.user.api.SysUserApi;
import org.jjche.sys.modules.user.api.vo.SysUserSimpleVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户服务，对内
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-25
 */
@Service
@RequiredArgsConstructor
public class SysUserApiImpl implements SysUserApi {
    private final UserService userService;
    @Override
    public List<SysUserSimpleVO> querySimple() {
        return userService.getBaseMapper().querySimple();
    }
}
