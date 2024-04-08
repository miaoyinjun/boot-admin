package org.jjche.sys.modules.system.dto;

import lombok.Data;
import org.jjche.common.dto.DeptSmallDTO;

import java.io.Serializable;
import java.util.List;

/**
 * <p>UserDO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
@Data
public class UserDTO implements Serializable {

    private Long id;

    private List<Long> roleIds;

    private List<Long> jobIds;

    private DeptSmallDTO dept;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    private Boolean enabled;
}
