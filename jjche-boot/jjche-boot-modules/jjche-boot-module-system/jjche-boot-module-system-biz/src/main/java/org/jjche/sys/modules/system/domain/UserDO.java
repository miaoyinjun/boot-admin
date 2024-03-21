package org.jjche.sys.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.mybatis.base.entity.BaseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

/**
 * <p>UserDO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user", resultMap = "BaseResultMap")
public class UserDO extends BaseEntity {

    private Long deptId;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门")
    private DeptDO dept;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门")
    private Set<RoleDO> roles;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户岗位")
    private Set<JobDO> jobs;

    @NotBlank
    @ApiModelProperty(value = "用户名称")
    private String username;

    @NotBlank
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @Email
    @NotBlank
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank
    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "头像真实名称", hidden = true)
    private String avatarName;

    @ApiModelProperty(value = "头像存储的路径", hidden = true)
    private String avatarPath;

    @ApiModelProperty(value = "密码")
    private String password;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

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

    @ApiModelProperty(value = "是否为admin账号", hidden = true)
    private Boolean isAdmin = false;

    @ApiModelProperty(value = "最后修改密码的时间", hidden = true)
    private Timestamp pwdResetTime;

    @ApiModelProperty(value = "最后一次登陆时间", hidden = true)
    private Timestamp lastLoginTime;
}
