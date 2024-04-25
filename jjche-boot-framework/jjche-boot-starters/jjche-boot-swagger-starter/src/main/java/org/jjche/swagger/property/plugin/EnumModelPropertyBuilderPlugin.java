package org.jjche.swagger.property.plugin;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.jjche.common.constant.EnumConstant;
import org.jjche.common.base.IBaseEnum;
import org.springframework.util.ReflectionUtils;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * swagger枚举出参定义说明
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@SuppressWarnings(value = "all")
public class EnumModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(ModelPropertyContext context) {
        Optional<BeanPropertyDefinition> optional = context.getBeanPropertyDefinition();
        if (!optional.isPresent()) {
            return;
        }
        if (ObjectUtil.isNull(optional.get().getField())) {
            return;
        }
        try {
            final Class<?> fieldType = optional.get().getField().getRawType();
            addDescForEnum(context, fieldType);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private void addDescForEnum(ModelPropertyContext context, Class<?> fieldType) {
        if (ObjectUtil.isNotNull(fieldType) && IBaseEnum.class.isAssignableFrom(fieldType)) {
            Object[] enumConstants = fieldType.getEnumConstants();

            List<String> displayValues =
                    Arrays.stream(enumConstants)
                            .filter(Objects::nonNull)
                            .map(item -> {
                                Class<?> currentClass = item.getClass();

                                Field indexField = ReflectionUtils.findField(currentClass, EnumConstant.VALUE);
                                ReflectionUtils.makeAccessible(indexField);
                                Object value = ReflectionUtils.getField(indexField, item);

                                Field descField = ReflectionUtils.findField(currentClass, EnumConstant.DESC);
                                if (descField != null) {
                                    ReflectionUtils.makeAccessible(descField);
                                    Object desc = ReflectionUtils.getField(descField, item);
                                    return value + ":" + desc;
                                }
                                return String.valueOf(value);
                            }).collect(Collectors.toList());


            ModelPropertyBuilder builder = context.getBuilder();
            Field descField = ReflectionUtils.findField(builder.getClass(), "description");
            ReflectionUtils.makeAccessible(descField);
            String joinText = ReflectionUtils.getField(descField, builder)
                    + "(" + String.join(";", displayValues) + ")";

            context.getSpecificationBuilder().description(joinText);

        }
    }
}
