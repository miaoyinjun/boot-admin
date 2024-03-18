package org.jjche.system.modules.user.api;

import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.system.constant.SysBaseApiConstants;
import org.jjche.system.modules.user.api.vo.UserExtVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * 用户 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
@Component
@FeignClient(contextId = SysBaseApiConstants.NAME + "-test-user-api",
        name = SysBaseApiConstants.NAME,
        path = "/sys/users/ext/",
        url = "${FEIGN_URL_SYSTEM:}"
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
    R<MyPage<UserExtVO>> page(PageParam pageable);
}
