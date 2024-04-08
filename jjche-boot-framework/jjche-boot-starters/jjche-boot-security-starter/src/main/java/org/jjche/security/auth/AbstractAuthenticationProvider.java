package org.jjche.security.auth;

import org.jjche.security.auth.sms.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>
 * 自定义AuthenticationProvider，验证用户名或手机号或唯一标识等值
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-11
 */
public class AbstractAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());

        this.additionalAuthenticationChecks(user, authentication);

        this.check(user);

        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authentication.getDetails());

        return authenticationResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    /**
     * <p>Getter for the field <code>userDetailsService</code>.</p>
     *
     * @return a {@link org.springframework.security.core.userdetails.UserDetailsService} object.
     */
    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    /**
     * <p>Setter for the field <code>userDetailsService</code>.</p>
     *
     * @param userDetailsService a {@link org.springframework.security.core.userdetails.UserDetailsService} object.
     */
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * <p>
     * 额外验证内容
     * </p>
     *
     * @param user 用户
     * @param auth 认证
     */
    protected void additionalAuthenticationChecks(UserDetails user, Authentication auth) {
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
    }

    /**
     * <p>
     * 检查常用用户标识
     * </p>
     *
     * @param user 用户
     */
    protected void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        } else if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }
    }
}
