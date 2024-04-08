package org.jjche.sys.modules.security.service;

import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.vo.UserVO;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.wrapper.constant.HttpStatusConstant;
import org.jjche.security.service.JwtUserService;
import org.jjche.sys.modules.system.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>UserDetailsServiceImpl class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
@Service(SecurityConstant.USER_DETAILS_PWD_SERVICE)
@ConditionalOnMissingBean(type = SecurityConstant.USER_DETAILS_PWD_SERVICE)
public class UserDetailsServiceImpl extends AbstractUserDetailsService {
    /**
     * <p>Constructor for UserDetailsServiceImpl.</p>
     *
     * @param userService    a {@link UserService} object.
     * @param roleService    a {@link RoleService} object.
     * @param dataService    a {@link DataService} object.
     * @param jwtUserService a {@link JwtUserService} object.
     * @param deptService    a {@link DeptService} object.
     * @param jobService     a {@link JobService} object.
     */
    public UserDetailsServiceImpl(UserService userService,
                                  RoleService roleService,
                                  DataService dataService,
                                  JwtUserService jwtUserService,
                                  DeptService deptService,
                                  JobService jobService) {
        super(userService, deptService, jobService, roleService, dataService, jwtUserService);
    }

    @Override
    UserVO findUserDto(String username) {
        UserVO userDto = userService.findByName(username);
        if (userDto == null) {
            throw new UsernameNotFoundException(HttpStatusConstant.MSG_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS);
        }
        userDto.setUserType(UserTypeEnum.PWD);
        return userDto;
    }
}
