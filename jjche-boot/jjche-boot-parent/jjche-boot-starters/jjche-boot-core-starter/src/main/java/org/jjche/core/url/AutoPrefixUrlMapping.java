package org.jjche.core.url;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.jjche.common.constant.PackageConstant;
import org.jjche.core.annotation.controller.OutRestController;
import org.jjche.core.property.CoreApiPathProperties;
import org.jjche.core.property.CoreProperties;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>
 * Url前缀
 * </p>
 *
 * @author miaoyj
 * @since 2022-04-29
 */
public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {
    @Resource
    private CoreProperties coreProperties;

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        CoreApiPathProperties coreApiPathProperties = coreProperties.getApi().getPath();
        //获取url前缀
        RequestMappingInfo requestMappingInfo = super.getMappingForMethod(method, handlerType);
        String packageName = handlerType.getName();
        boolean apiRest = AnnotationUtil.hasAnnotation(handlerType, RestController.class);
        if ((StrUtil.startWith(packageName, PackageConstant.BASE_PATH) && apiRest) && null != requestMappingInfo) {
            /**
             * 单体前缀：/api
             * 微服务前缀：/，由网关转发
             */
            String prefix = coreApiPathProperties.getPrefix();
            List<String> packageList = StrUtil.split(packageName, StrUtil.C_DOT);
            /**
             * org.jjche.bpm.modules.definition.rest.BpmModelController
             * 获取路径中的bpm作为路径前缀
             */
            prefix += StrUtil.SLASH + CollUtil.get(packageList, 2);
            boolean outRest = AnnotationUtil.hasAnnotation(handlerType, OutRestController.class);
            if (outRest) {
                prefix += coreApiPathProperties.getOutPrefix();
            }
            //根据url前缀生成RequestMappingInfo并与原有的RequestMappingInfo合并
            requestMappingInfo = RequestMappingInfo.paths(prefix).build().combine(requestMappingInfo);
        }
        return requestMappingInfo;
    }
}
