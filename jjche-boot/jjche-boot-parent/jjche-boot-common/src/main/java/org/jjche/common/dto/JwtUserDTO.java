package org.jjche.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jjche.common.vo.DataScopeVO;
import org.jjche.common.vo.UserVO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>JwtUserDto class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Data
@NoArgsConstructor
public class JwtUserDTO implements UserDetails {

    private UserVO user = null;

    private DataScopeVO dataScopeVO = null;
    private List<SimpleGrantedAuthorityDTO> authorities = null;

    /**
     * <p>Constructor for JwtUserDto.</p>
     *
     * @param user        a {@link UserVO} object.
     * @param dataScopeVO   a {@link java.util.List} object.
     * @param authorities a {@link java.util.List} object.
     */
    public JwtUserDTO(UserVO user, DataScopeVO dataScopeVO, List<SimpleGrantedAuthorityDTO> authorities) {
        this.user = user;
        this.dataScopeVO = dataScopeVO;
        this.authorities = authorities;
    }

    /**
     * <p>getRoles.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getRoles() {
        return authorities.stream().map(SimpleGrantedAuthorityDTO::getAuthority).collect(Collectors.toSet());
    }

    public void setRoles(Set<String> roles) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return user.getIsAccountNonExpired();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return user.getIsAccountNonLocked();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return user.getIsCredentialsNonExpired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
