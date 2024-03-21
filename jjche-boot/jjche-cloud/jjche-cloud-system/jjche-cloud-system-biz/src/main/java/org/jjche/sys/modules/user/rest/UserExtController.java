package org.jjche.sys.modules.user.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.sys.modules.user.api.vo.UserExtVO;
import org.jjche.sys.modules.user.service.UserExtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统：用户扩展")
@RestController
@RequestMapping("users/ext")
@RequiredArgsConstructor
public class UserExtController extends BaseController  {
    private final UserExtService userExtService;
    @ApiOperation("测试-查询用户")
    @GetMapping
    public R<MyPage<UserExtVO>> page(PageParam pageable) {
        return R.ok(userExtService.page(pageable));
    }

}
