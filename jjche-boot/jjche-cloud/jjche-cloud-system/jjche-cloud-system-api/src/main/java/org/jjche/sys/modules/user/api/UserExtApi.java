package org.jjche.sys.modules.user.api;

import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.sys.constant.SysBaseApiConstants;
import org.jjche.sys.modules.user.api.vo.UserExtVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * 用户 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
@FeignClient(contextId = SysBaseApiConstants.NAME + "-user-ext-api",
        name = SysBaseApiConstants.NAME,
        path = "/sys/users/ext/",
        url = SysBaseApiConstants.FEIGN_URL
)
public interface UserExtApi {

    /**
     * <p>
     * 查询用户
     * </p>
     *
     * @param pageable
    * @return /
     */
    @GetMapping
    R<MyPage<UserExtVO>> page(@SpringQueryMap PageParam pageable);
}
