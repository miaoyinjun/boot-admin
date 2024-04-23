package org.jjche.core.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jjche.common.exception.BusinessException;
import org.jjche.common.exception.enums.GlobalErrorCodeEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认 Controller，解决部分 module 未开启时的 404 提示。
 * 例如说，/bpm/** 路径，工作流
 *
 * @author 芋道源码
 */
@Api(hidden = true)
@RestController
public class DefaultController {

    @RequestMapping("/bpm/**")
    @ApiOperation(value = "bpm", hidden = true)
    public void bpm404() {
        throw new BusinessException(GlobalErrorCodeEnum.NOT_IMPLEMENTED.getCode(),
         "[工作流模块 jjche-boot-module-bpm - 已禁用][参考 https://www.yuque.com/miaoyj/nsln4r/wfakr6frz33peu15开启]");
    }
}
