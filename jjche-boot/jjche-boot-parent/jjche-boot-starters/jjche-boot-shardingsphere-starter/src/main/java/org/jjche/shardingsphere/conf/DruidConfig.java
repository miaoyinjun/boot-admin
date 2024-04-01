package org.jjche.shardingsphere.conf;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.jjche.common.util.StrUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 数据源配置
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-26
 */
@Configuration
public class DruidConfig {
    /**
     * Druid监控
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        String urlMappings = SpringUtil.getProperty("spring.datasource.druid.stat-view-servlet.url-pattern");
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), urlMappings);
        Map<String, String> initParams = new HashMap<>(5);
        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * web监控的filter
     */
    @Bean
    public FilterRegistrationBean webStatFilter() {
        String globalPrefix = SpringUtil.getProperty("jjche.core.api.path.prefix");
        String prefix = SpringUtil.getProperty("jjche.core.api.path.prefix");
        String urlPatterns = StrUtil.format("{}{}/*", globalPrefix, prefix);
        urlPatterns = StrUtil.replace(urlPatterns, "//", "/");
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>(5);
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList(urlPatterns));
        return bean;
    }
}
