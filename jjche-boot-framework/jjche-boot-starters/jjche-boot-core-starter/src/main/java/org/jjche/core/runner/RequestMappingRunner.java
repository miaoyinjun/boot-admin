package org.jjche.core.runner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 请求映射初始化到内存
 * </p>
 *
 * @author miaoyj
 * @since 2023-01-29
 */
@Component
@RequiredArgsConstructor
public class RequestMappingRunner implements ApplicationRunner {

    /**
     * Constant <code>DICT_ENUMS</code>
     */
    public final static Map<PatternsRequestCondition, Set<String>> MAPPING_INFO_MAP = MapUtil.newHashMap();

    /**
     * {@inheritDoc}
     * <p>
     * 项目启动时 请求映射保存到内存
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        StaticLog.info("--------------------请求映射初始化到内存---------------------");
        RequestMappingHandlerMapping mapping = SpringUtil.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            PatternsRequestCondition p = info.getPatternsCondition();
            Set<RequestMethod> methods = m.getKey().getMethodsCondition().getMethods();
            Set<String> methodsValueMap = MAPPING_INFO_MAP.get(p);
            if (CollUtil.isEmpty(methodsValueMap)) {
                methodsValueMap = CollUtil.newHashSet();
            }
            Set<String> methodsNew = methods.stream().map(RequestMethod::name).collect(Collectors.toSet());
            methodsValueMap.addAll(methodsNew);
            MAPPING_INFO_MAP.put(p, methodsValueMap);
        }
        StaticLog.info("--------------------请求映射初始化到内存完成---------------------");
    }
}
