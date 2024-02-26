package org.jjche.core.rest;

import org.jjche.common.exception.BusinessException;
import org.jjche.core.annotation.controller.SysRestController;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 默认 Controller，解决部分 module 未开启时的 404 提示。
 * 例如说，/bpm/** 路径，工作流
 *
 * @author 芋道源码
 */
@SysRestController
public class DefaultController {

    @RequestMapping("/bpm/**")
    public void bpm404() {
        throw new BusinessException
         ("[工作流模块 yudao-module-bpm - 已禁用][参考 https://doc.iocoder.cn/bpm/ 开启]");
    }
}
