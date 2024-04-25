package org.jjche.sys.modules.system.api;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jjche.core.base.BaseController;
import org.jjche.sys.modules.user.api.SysUserApi;
import org.jjche.sys.modules.user.api.vo.SysUserSimpleVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户服务,对内
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-25
 */
@Api(tags = "用户对内")
@ApiSupport(order = 1, author = "miaoyj")
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class SysUserApiController extends BaseController {

    private final SysUserApi sysUserApi;
    /**
     * <p>
     * 查询用户简单
     * </p>
     *
     * @return /
     */
    @GetMapping("simple")
    List<SysUserSimpleVO> querySimple(){
        return sysUserApi.querySimple();
    }
}
