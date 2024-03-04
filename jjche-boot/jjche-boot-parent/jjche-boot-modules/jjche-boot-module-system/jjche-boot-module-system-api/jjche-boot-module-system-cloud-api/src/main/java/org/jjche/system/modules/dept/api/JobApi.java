package org.jjche.system.modules.dept.api;

import org.jjche.system.constant.ApiConstants;
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
@FeignClient(contextId = ApiConstants.NAME + "-job-api",
        name = ApiConstants.NAME,
        path = "/sys/job/",
        url = "${FEIGN_URL_SYSTEM:}"
)
public interface JobApi {

    /**
     * 校验岗位们是否有效。如下情况，视为无效：
     * 1. 岗位编号不存在
     * 2. 岗位被禁用
     *
     * @param ids 岗位编号数组
     */
    @GetMapping("valid")
    void validPostList(@RequestParam Collection<Long> ids);
}
