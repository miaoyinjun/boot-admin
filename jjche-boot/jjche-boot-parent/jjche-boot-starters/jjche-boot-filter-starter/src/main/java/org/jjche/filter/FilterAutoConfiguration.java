package org.jjche.filter;

import cn.hutool.core.collection.CollUtil;
import org.jjche.cache.service.RedisService;
import org.jjche.common.api.CommonAppKeyApi;
import org.jjche.filter.enc.api.EncCheckHeaderInterceptor;
import org.jjche.filter.enc.field.EncryptFieldAop;
import org.jjche.filter.enc.field.EncryptFieldUtil;
import org.jjche.filter.enc.limit.LimitAspect;
import org.jjche.filter.property.FilterEncProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-10
 */
@Configuration
@Import({FilterEncProperties.class, LimitAspect.class,
        EncryptFieldAop.class, EncryptFieldUtil.class})
public class FilterAutoConfiguration {
    @Autowired
    private FilterEncProperties filterEncProperties;
    @Autowired
    private RedisService redisService;
    @Autowired
    @Lazy
    private CommonAppKeyApi commonAppKeyApi;

    /**
     * <p>
     * 过滤器配置
     * </p>
     *
     * @return a {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer} object.
     * @author miaoyj
     * @since 2020-08-13
     */
    @Bean("FilterEncWebMvcConfigure")
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 添加拦截器
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //全局上下文
                registry.addInterceptor(new ContextInterceptor());
                //对外
                List<String> filterUrls = filterEncProperties.getUrls();
                InterceptorRegistration checkHeaderInterceptorRegistration = null;
                /** 默认拦截项目中定义的接口地址*/
                EncCheckHeaderInterceptor checkParamInterceptor = new EncCheckHeaderInterceptor(commonAppKeyApi, redisService);
                checkHeaderInterceptorRegistration = registry.addInterceptor(checkParamInterceptor);
                /**添加要过滤的URL*/
                if (CollUtil.isNotEmpty(filterUrls)) {
                    checkHeaderInterceptorRegistration.addPathPatterns(filterUrls);
                }
            }
        };
    }
}
