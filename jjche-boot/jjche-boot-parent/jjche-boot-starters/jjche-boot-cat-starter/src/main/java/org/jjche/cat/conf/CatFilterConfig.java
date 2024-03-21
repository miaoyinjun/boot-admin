package org.jjche.cat.conf;

import org.jjche.cat.integration.filter.CatServerFilter;
import org.jjche.common.util.StrUtil;
import org.jjche.core.property.CoreApiPathProperties;
import org.jjche.core.property.CoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <p>
 * cat的的核心过滤器
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-13
 */
@Configuration
public class CatFilterConfig {
    @Resource
    private CoreProperties coreProperties;
    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Bean
    public FilterRegistrationBean catFilter() {
        String urlPatterns = this.getUrlPatterns();
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CatServerFilter catFilter = new CatServerFilter(applicationName);
        registration.setFilter(catFilter);
        registration.addUrlPatterns(urlPatterns);
        registration.setOrder(1);
        return registration;
    }

    /**
     * <p>
     * 获取api前缀
     * </p>
     *
     * @return /
     */
    private String getUrlPatterns() {
        CoreApiPathProperties coreApiPathProperties = coreProperties.getApi().getPath();
        //获取url前缀
        String globalPrefix = coreApiPathProperties.getPrefix();
        String urlPatterns = StrUtil.format("{}/*", globalPrefix);
        urlPatterns = StrUtil.replace(urlPatterns, "//", "/");
        return urlPatterns;
    }
}
