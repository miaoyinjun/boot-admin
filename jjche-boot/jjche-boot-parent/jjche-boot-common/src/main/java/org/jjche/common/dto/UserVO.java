package org.jjche.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.jjche.common.enums.UserTypeEnum;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>UserVO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Data
public class UserVO implements Serializable {

    private Long id;

    private List<RoleSmallDto> roles;

    private List<JobSmallDto> jobs;

    private DeptSmallDto dept;

    private Long deptId;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    /**
     * 创建时间
     */
    private Timestamp gmtCreate;

    private Timestamp lastLoginTime;

    @JsonIgnore
    private String password;

    /**
     * 是否必须修改密码
     */
    private Boolean isMustResetPwd;

    /**
     * 是否提示修改密码
     */
    private Boolean isTipResetPwd;

    /**
     * 密码连续错误次数
     */
    @JsonIgnore
    private Integer pwdFailsCount;

    /**
     * 账号是否未过期
     */
    @JsonIgnore
    private Boolean isAccountNonExpired;
    /**
     * 账号是否未锁定
     */
    @JsonIgnore
    private Boolean isAccountNonLocked;
    /**
     * 账号凭证是否未过期
     */
    @JsonIgnore
    private Boolean isCredentialsNonExpired;

    private Boolean enabled;

    @JsonIgnore
    private Boolean isAdmin;

    private UserTypeEnum userType;
}