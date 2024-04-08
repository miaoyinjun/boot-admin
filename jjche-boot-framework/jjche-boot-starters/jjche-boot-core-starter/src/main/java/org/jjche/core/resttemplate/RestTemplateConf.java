package org.jjche.core.resttemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * <p>
 * RestTemplate 配置
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-24
 */
@Configuration
@RequiredArgsConstructor
public class RestTemplateConf {
    private final FeignClientProperties feignProperties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        Map<String, FeignClientProperties.FeignClientConfiguration> feignConfMap = feignProperties.getConfig();
        FeignClientProperties.FeignClientConfiguration feignConf = feignConfMap.get(feignProperties.getDefaultConfig());
        Integer connectTimeout = feignConf.getConnectTimeout();
        Integer readTimeout = feignConf.getReadTimeout();
        builder = builder
                //设置连接超时时间
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                //设置读取超时时间
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .additionalInterceptors(new RequestResponseLoggingInterceptor())
                .messageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return builder.build();
    }
}