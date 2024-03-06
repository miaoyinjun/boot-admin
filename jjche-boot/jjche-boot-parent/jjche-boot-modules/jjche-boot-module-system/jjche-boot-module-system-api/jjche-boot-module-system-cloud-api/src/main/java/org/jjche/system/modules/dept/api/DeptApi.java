package org.jjche.system.modules.dept.api;

import org.jjche.common.dto.DeptSmallDTO;
import org.jjche.system.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    /**
     * 根据ID查询
     *
     * @param ids ID
     * @return /
     */
    @GetMapping("list-small-condition")
    List<DeptSmallDTO> listByIds(@RequestParam Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    @GetMapping("get-small")
    DeptSmallDTO getSmallById(@RequestParam Long id);

    /**
     * <p>
     * 获取所有精简部门
     * </p>
     *
     * @return /
     */
    @GetMapping("list-small")
    List<DeptSmallDTO> listSmall();
}
