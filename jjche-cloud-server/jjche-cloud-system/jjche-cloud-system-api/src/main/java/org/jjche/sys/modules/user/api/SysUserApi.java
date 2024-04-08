package org.jjche.sys.modules.user.api;

import org.jjche.common.constant.SysBaseApiConstants;
import org.jjche.sys.modules.user.api.vo.SysUserSimpleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 * 用户服务
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-25
 */
@FeignClient(contextId = SysBaseApiConstants.NAME + "-user-api",
        name = SysBaseApiConstants.NAME,
        path = "/sys/internal/users/",
        url = SysBaseApiConstants.FEIGN_URL)
public interface SysUserApi {

    /**
     * <p>
     * 查询用户简单
     * </p>
     *
    * @return /
     */
    @GetMapping("simple")
    List<SysUserSimpleVO> querySimple();
}
