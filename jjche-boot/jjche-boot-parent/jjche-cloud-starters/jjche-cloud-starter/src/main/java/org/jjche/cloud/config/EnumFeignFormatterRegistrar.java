package org.jjche.cloud.config;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;

/**
 * <p>
 * feign枚举转换
 * </p>
 *
 * @author miaoyj
 * @since 2022-04-27
 */
@Component
public class EnumFeignFormatterRegistrar implements FeignFormatterRegistrar {

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addConverter(new UniversalReversedEnumConverter());
    }

}
