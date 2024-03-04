package org.jjche.common.system.api.dept;

import org.jjche.common.system.api.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

/**
 * <p>
 * 部门 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
@Component
@FeignClient(contextId = ApiConstants.NAME + "-dept-api",
        name = ApiConstants.NAME,
        path = "/sys/dept/",
        url = "${FEIGN_URL_SYSTEM:}"
)
public interface DeptApi {

    /**
     * 校验部门们是否有效。如下情况，视为无效：
     * 1. 部门编号不存在
     * 2. 部门被禁用
     *
     * @param ids 角色编号数组
     */
    @GetMapping("valid")
    void validateDeptList(@RequestParam Collection<Long> ids);

}
