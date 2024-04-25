package org.jjche.security.auth.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * <p>
 * 短信验证码登录校验类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-11
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;


    private final Object principal;


    /**
     * <p>Constructor for SmsCodeAuthenticationToken.</p>
     *
     * @param mobile a {@link java.lang.String} object.
     */
    public SmsCodeAuthenticationToken(String mobile) {
        super(null);
        this.principal = mobile;
        setAuthenticated(false);
    }


    /**
     * <p>Constructor for SmsCodeAuthenticationToken.</p>
     *
     * @param principal   a {@link java.lang.Object} object.
     * @param authorities a {@link java.util.Collection} object.
     */
    public SmsCodeAuthenticationToken(Object principal,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
