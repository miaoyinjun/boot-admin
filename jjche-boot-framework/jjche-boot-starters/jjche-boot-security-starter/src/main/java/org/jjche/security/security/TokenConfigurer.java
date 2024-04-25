package org.jjche.security.security;

import lombok.RequiredArgsConstructor;
import org.jjche.common.api.CommonApi;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>TokenConfigurer class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final CommonApi commonApi;

    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(commonApi);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
