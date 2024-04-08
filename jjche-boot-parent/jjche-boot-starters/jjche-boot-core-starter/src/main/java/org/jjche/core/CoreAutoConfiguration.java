package org.jjche.core;

import org.jjche.core.alarm.dd.AlarmDingTalkService;
import org.jjche.core.exception.GlobalExceptionHandler;
import org.jjche.core.filter.RequestWrapperFilter;
import org.jjche.core.property.CoreProperties;
import org.jjche.core.url.AutoPrefixUrlMapping;
import org.jjche.core.util.SpringContextHolder;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Import({GlobalExceptionHandler.class,
        CoreProperties.class, cn.hutool.extra.spring.SpringUtil.class,
        RequestWrapperFilter.class, AlarmDingTalkService.class})
@Configuration
@ComponentScan(basePackages = {"cn.hutool.extra.spring"})
@EnableAsync(proxyTargetClass = true)
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableScheduling
public class CoreAutoConfiguration implements WebMvcConfigurer, WebMvcRegistrations {

    @Resource
    private CoreProperties coreProperties;

    /**
     * {@inheritDoc}
     *
     * <p>
     * 增加restApi前缀
     * </p>
     * <p>
     * ApiRestController与SysRestController
     * </p>
     *
     * @author miaoyj
     * @since 2020-09-21
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
//        CoreApiPathProperties coreApiPathProperties = coreProperties.getApi().getPath();
//        configurer
//                .addPathPrefix(coreApiPathProperties.getGlobalPrefix(), c -> c.isAnnotationPresent(ApiRestController.class))
//                .addPathPrefix(coreApiPathProperties.getSysPrefix(), c -> c.isAnnotationPresent(SysRestController.class));
    }

    /**
     * <p>springContextHolder.</p>
     *
     * @return a {@link SpringContextHolder} object.
     */
//    @Bean
//    public SpringContextHolder springContextHolder() {
//        return new SpringContextHolder();
//    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new AutoPrefixUrlMapping();
    }
}
