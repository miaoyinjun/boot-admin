package org.jjche.sys.modules.user.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 部门简单
 * </p>
 *
 * @author miaoyj
 * @since 2023-03-22
 */
@Data
public class SysUserSimpleVO implements Serializable {
    private Long id;
    private String nickName;
}
