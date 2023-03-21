package org.jjche.system.modules.security.service;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.UserVO;
import org.jjche.security.service.JwtUserService;
import org.jjche.system.modules.system.service.*;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>UserDetailsServiceImpl class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
@RequiredArgsConstructor
public abstract class AbstractUserDetailsService implements UserDetailsService {
    protected final UserService userService;
    protected final DeptService deptService;
    protected final JobService jobService;
    private final RoleService roleService;
    private final DataService dataService;
    private final JwtUserService jwtUserService;

    /**
     * <p>
     * 查询用户
     * </p>
     *
     * @param username 用户名
     * @return 用户
     */
    abstract UserVO findUserDto(String username);

    /**
     * {@inheritDoc}
     */
    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = jwtUserService.getByUserName(username);
        if (ObjectUtil.isNull(jwtUserDto)) {
            UserVO user = this.findUserDto(username);
            if (user != null) {
                jwtUserDto = new JwtUserDto(
                        user,
                        dataService.getDataScope(user),
                        roleService.mapToGrantedAuthorities(user)
                );
                //防止用户状态不正常时被缓存
                Boolean enabled = user.getEnabled();
                Boolean isAccountNonExpired = user.getIsAccountNonExpired();
                Boolean isAccountNonLocked = user.getIsAccountNonLocked();
                Boolean isCredentialsNonExpired = user.getIsCredentialsNonExpired();
                if (enabled && isAccountNonExpired && isAccountNonLocked && isCredentialsNonExpired) {
                    jwtUserService.putByUserName(username, jwtUserDto);
                } else {
                    jwtUserService.removeByUserName(username);
                }
            }
        }
        return jwtUserDto;
    }
}
