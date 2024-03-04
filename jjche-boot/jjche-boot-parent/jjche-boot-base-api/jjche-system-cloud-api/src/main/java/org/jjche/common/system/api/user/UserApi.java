package org.jjche.common.system.api.user;

import org.jjche.common.system.api.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * 用户 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
@Component
@FeignClient(contextId = ApiConstants.NAME + "-user-api",
        name = ApiConstants.NAME,
        path = "/sys/users/",
        url = "${FEIGN_URL_SYSTEM:}"
)
public interface UserApi {

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    @GetMapping("valid")
    void validateUserList(@RequestParam Collection<Long> ids);

    /**
     * 获得指定岗位的用户数组
     *
     * @param jobIds 岗位数组
     * @return 用户id数组
     */
    @GetMapping("list-user-ids")
    Set<Long> listUserIdsByJobIds(@RequestParam Set<Long> jobIds);
}
