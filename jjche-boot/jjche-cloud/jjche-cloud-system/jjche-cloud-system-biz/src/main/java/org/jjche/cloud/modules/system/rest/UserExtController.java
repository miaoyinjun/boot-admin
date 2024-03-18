package org.jjche.cloud.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.cloud.modules.system.service.UserExtService;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.system.modules.user.api.UserExtApi;
import org.jjche.system.modules.user.api.vo.UserExtVO;
import org.springframework.web.bind.annotation.GetMapping;

@Api(tags = "系统：用户扩展")
@SysRestController("users/ext")
@RequiredArgsConstructor
public class UserExtController extends BaseController implements UserExtApi {
    private final UserExtService userExtService;
    @ApiOperation("查询用户")
    @GetMapping
    public R<MyPage<UserExtVO>> page(PageParam pageable) {
        return R.ok(userExtService.page(pageable));
    }
}
