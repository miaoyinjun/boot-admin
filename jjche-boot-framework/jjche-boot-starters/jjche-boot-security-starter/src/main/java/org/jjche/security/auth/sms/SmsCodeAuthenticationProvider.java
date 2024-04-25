package org.jjche.security.auth.sms;

import org.jjche.security.auth.AbstractAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * 手机验证码登录认证处理类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-11
 */
public class SmsCodeAuthenticationProvider extends AbstractAuthenticationProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails user, Authentication auth) {
    }
}
