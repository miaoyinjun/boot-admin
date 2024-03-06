package org.jjche.cloud.config;

import org.jjche.common.base.IBaseEnum;
import org.springframework.core.convert.converter.Converter;

/**
 * <p>
 * 枚举feign转换
 * </p>
 *
 * @author miaoyj
 * @since 2022-04-27
 */
public class UniversalReversedEnumConverter implements Converter<IBaseEnum, String> {

    @Override
    public String convert(IBaseEnum source) {
        return source.getValue();
    }
}
