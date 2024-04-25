/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package org.jjche.sys.modules.security.rest;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jjche.common.util.StrUtil;
import org.jjche.common.wrapper.response.R;
import org.jjche.security.annotation.rest.IgnorePostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 滑动验证码
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-01
 */
@Api(tags = "系统：滑动验证码")
@RestController
@RequestMapping("captcha")
@RequiredArgsConstructor
public class AuthCaptchaController {

    private final CaptchaService captchaService;

    public static String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StrUtil.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StrUtil.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StrUtil.trim(ipList[0]);
        }
        return null;
    }

    @IgnorePostMapping("/get")
    public R<ResponseModel> get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        assert request.getRemoteHost() != null;
        data.setBrowserInfo(getRemoteId(request));
        return R.ok(captchaService.get(data));
    }

    @IgnorePostMapping("/check")
    public R<ResponseModel> check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return R.ok(captchaService.check(data));
    }

}
