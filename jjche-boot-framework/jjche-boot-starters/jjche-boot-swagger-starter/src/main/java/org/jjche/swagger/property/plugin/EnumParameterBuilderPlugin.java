package org.jjche.swagger.property.plugin;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.base.Joiner;
import io.swagger.annotations.ApiParam;
import org.jjche.common.constant.EnumConstant;
import org.jjche.common.base.IBaseEnum;
import org.springframework.util.ReflectionUtils;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * swagger枚举入参定义说明
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@SuppressWarnings(value = "all")
public class EnumParameterBuilderPlugin implements ParameterBuilderPlugin {

    private static final Joiner joiner = Joiner.on(";");

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(ParameterContext context) {
        Optional<ApiParam> apiParam = context.resolvedMethodParameter().findAnnotation(ApiParam.class);
        if (apiParam.isPresent()) {
            ApiParam annotation = (ApiParam) apiParam.get();
            Class<?> type = context.resolvedMethodParameter().getParameterType().getErasedType();
            if (IBaseEnum.class.isAssignableFrom(type)) {
                Object[] enumConstants = type.getEnumConstants();
                List<String> displayValues = Arrays.stream(enumConstants).filter(Objects::nonNull).
                        filter(o -> ObjectUtil.isNotNull(ReflectionUtils.findField(o.getClass(), EnumConstant.VALUE))).map(item -> {
                            Class<?> currentClass = item.getClass();

                            Field indexField = ReflectionUtils.findField(currentClass, EnumConstant.VALUE);
                            ReflectionUtils.makeAccessible(indexField);
                            Object value = ReflectionUtils.getField(indexField, item);

                            Field descField = ReflectionUtils.findField(currentClass, EnumConstant.DESC);
                            ReflectionUtils.makeAccessible(descField);
                            Object desc = ReflectionUtils.getField(descField, item);
                            return value + ":" + desc;

                        }).collect(Collectors.toList());
                String desc = annotation.value() + "(" + joiner.join(displayValues) + ")";
                context.requestParameterBuilder()
                        .description(desc);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
