package org.jjche.system.modules.security.listener;

import org.jjche.security.service.JwtUserService;
import org.jjche.system.modules.system.domain.UserDO;
import org.jjche.system.modules.system.mapstruct.UserMapStruct;
import org.jjche.system.modules.system.service.UserService;
import org.jjche.system.property.AdminProperties;
import org.jjche.system.property.PasswordProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 登陆失败监听
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-01-06
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminProperties adminProperties;
    @Autowired
    private UserMapStruct userMapper;
    @Autowired
    private JwtUserService jwtUserService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        PasswordProperties password = adminProperties.getUser().getPassword();
        String username = authenticationFailureBadCredentialsEvent.getAuthentication().getPrincipal().toString();
        UserDO user = userService.getByUsername(username);
        if (user != null) {
            // 用户密码失败次数
            Integer fails = user.getPwdFailsCount();
            if (fails == null) {
                fails = 0;
            }
            fails++;
            user.setPwdFailsCount(fails);
            // 超出失败pwdFailsMaxCount次，锁定账户
            if (fails >= password.getFailsMaxCount()) {
                user.setIsAccountNonLocked(false);
                jwtUserService.removeByUserName(username);
            }
            userService.saveUser(user);
        }
    }
}
