package org.jjche.sys.modules.security.rest;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.RsaUtils;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.security.property.SecurityProperties;
import org.jjche.sys.modules.system.api.dto.OnlineUserDTO;
import org.jjche.sys.modules.system.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>OnlineController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
@Api(tags = "系统：在线用户管理")
@RestController
@RequestMapping("/auth/online")
@RequiredArgsConstructor
public class OnlineController extends BaseController {

    private final AuthService authService;
    private final SecurityProperties securityProperties;

    /**
     * <p>query.</p>
     *
     * @param filter   a {@link java.lang.String} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询在线用户")
    @GetMapping
    @PreAuthorize("@el.check('online:list')")
    public R query(String filter, PageParam pageable) {
        List<OnlineUserDTO> onlineUserDTOS = authService.getAll(filter);
        List<OnlineUserDTO> list = CollUtil.page((int) pageable.getPageIndex() - 1, (int) pageable.getPageSize(), onlineUserDTOS);
        MyPage myPage = new MyPage();
        myPage.setRecords(list);
        myPage.setTotal(onlineUserDTOS.size());
        return R.ok(myPage);
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param filter   a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    @LogRecord(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "在线用户"
    )
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('online:list')")
    public void download(HttpServletResponse response, String filter) throws IOException {
        authService.download(authService.getAll(filter), response);
    }

    /**
     * <p>delete.</p>
     *
     * @param keys a {@link java.util.Set} object.
     * @return a {@link R} object.
     * @throws java.lang.Exception if any.
     */
    @ApiOperation("踢出用户")
    @DeleteMapping
    @PreAuthorize("@el.check('online:del')")
    public R delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            String privateKey = securityProperties.getRsa().getPrivateKey();
            // 解密Key
            key = RsaUtils.decryptByPrivateKey(privateKey, key);
            authService.kickOut(key);
        }
        return R.ok();
    }
}
