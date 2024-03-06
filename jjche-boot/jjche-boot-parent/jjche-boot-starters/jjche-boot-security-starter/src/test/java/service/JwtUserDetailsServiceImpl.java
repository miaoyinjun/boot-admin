package service;

import org.jjche.common.dto.DeptSmallDTO;
import org.jjche.common.dto.JwtUserDTO;
import org.jjche.common.dto.SimpleGrantedAuthorityDTO;
import org.jjche.common.dto.UserVO;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 用户认证
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Service
@Configuration
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    public static String username = "admin";
    public static String usernameEnabled = "admin1";
    public static String usernameAccountNonLocked = "admin2";
    public static String usernameAccountNonExpired = "admin3";
    public static String usernameCredentialsNonExpired = "admin4";

    public static String password = "admin";

    static Map<String, JwtUserDTO> userDtoCache = new ConcurrentHashMap<>();

    @Override
    public JwtUserDTO loadUserByUsername(String username) {
        List<SimpleGrantedAuthorityDTO> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthorityDTO("ROLE_USER"));
        String password = "$2a$10$DOCWRJejaEnhW1p7Ez4wEePbhQcdJkOOnEb17VdhMr1wQtZGAk.zi";
        UserVO user = new UserVO();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);
        DeptSmallDTO deptSmallDto = new DeptSmallDTO();
        deptSmallDto.setId(1L);
        deptSmallDto.setName("aaa");
        user.setDept(deptSmallDto);
        user.setEnabled(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        JwtUserDTO jwtUserDto = new JwtUserDTO(
                user,
                null,
                authorityList
        );
        if (username.equalsIgnoreCase(usernameEnabled)) {
            user.setEnabled(false);
        } else if (username.equalsIgnoreCase(usernameCredentialsNonExpired)) {
            user.setIsCredentialsNonExpired(false);
        } else if (username.equalsIgnoreCase(usernameAccountNonLocked)) {
            user.setIsAccountNonLocked(false);
        } else if (username.equalsIgnoreCase(usernameAccountNonExpired)) {
            user.setIsAccountNonExpired(false);
        }
        userDtoCache.put(username, jwtUserDto);
        return jwtUserDto;
    }
}
