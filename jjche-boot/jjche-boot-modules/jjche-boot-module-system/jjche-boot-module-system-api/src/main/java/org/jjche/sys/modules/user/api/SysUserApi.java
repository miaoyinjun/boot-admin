package org.jjche.sys.modules.user.api;

import org.jjche.sys.modules.user.api.vo.SysUserSimpleVO;

import java.util.List;

/**
 * <p>
 * 用户对内服务，feign
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-25
 */
public interface SysUserApi {

    /**
     * <p>
     * 查询用户简单
     * </p>
     *
    * @return /
     */
    List<SysUserSimpleVO> querySimple();
}
