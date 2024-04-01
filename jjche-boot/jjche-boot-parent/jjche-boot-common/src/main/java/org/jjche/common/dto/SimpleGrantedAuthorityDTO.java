package org.jjche.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * <p>
 * 解决feign传输序列化
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleGrantedAuthorityDTO implements GrantedAuthority {
    private String role;

    @Override
    public String getAuthority() {
        return this.role;
    }
}
