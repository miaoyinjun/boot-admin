package org.jjche.system.modules.security.service;

import cn.hutool.core.lang.Assert;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.UserVO;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.security.service.JwtUserService;
import org.jjche.system.modules.system.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 手机号登录
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-11
 */
@Service(SecurityConstant.USER_DETAILS_SMS_SERVICE)
@ConditionalOnMissingBean(type = SecurityConstant.USER_DETAILS_SMS_SERVICE)
public class SmsUserDetailsServiceImpl extends AbstractUserDetailsService {

    /**
     * <p>Constructor for SmsUserDetailsServiceImpl.</p>
     *
     * @param userService    a {@link UserService} object.
     * @param roleService    a {@link RoleService} object.
     * @param dataService    a {@link DataService} object.
     * @param jwtUserService a {@link JwtUserService} object.
     * @param deptService    a {@link DeptService} object.
     * @param jobService     a {@link JobService} object.
     */
    public SmsUserDetailsServiceImpl(UserService userService,
                                     RoleService roleService,
                                     DataService dataService,
                                     JwtUserService jwtUserService,
                                     DeptService deptService,
                                     JobService jobService) {
        super(userService, deptService, jobService, roleService, dataService, jwtUserService);
    }

    @Override
    UserVO findUserDto(String username) {
        UserVO user = userService.findDtoByPhone(username);
        Assert.notNull(user, "未找到该手机号用户");
        //重置用户名为手机号
        user.setUsername(username);
        user.setUserType(UserTypeEnum.SMS);
        return user;
    }
}
