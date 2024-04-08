package org.jjche.cat.conf;

import org.jjche.cat.integration.rest.RestTemplateRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * RestTemplate 配置
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-24
 */
@Configuration
public class RestTemplateAutoConfig {
    @Autowired(required = false)
    private List<RestTemplate> restTemplates;

    /**
     * RestTemplate 拦截器
     *
     * @return /
     */
    @Bean
    public RestTemplateRequestInterceptor restTemplateRequestInterceptor() {
        RestTemplateRequestInterceptor restTemplateRequestInterceptor = new RestTemplateRequestInterceptor();
        if (restTemplates != null) {
            restTemplates.stream().forEach(restTemplate -> {
                List<ClientHttpRequestInterceptor> list = new ArrayList<>(restTemplate.getInterceptors());
                list.add(restTemplateRequestInterceptor);
                restTemplate.setInterceptors(list);
            });
        }
        return restTemplateRequestInterceptor;
    }
}