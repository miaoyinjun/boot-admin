package org.jjche.cloud.config;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;
import feign.Param;
import feign.QueryMapEncoder;
import feign.codec.EncodeException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Feign对SpringQueryMap自定义处理
 * </p>
 *
 * @author miaoyj
 * @since 2022-04-27
 */
@Configuration
@Component
public class FeignFieldQueryMapEncoder implements QueryMapEncoder {
    @Override
    public Map<String, Object> encode(Object object) throws EncodeException {
        Class<?> cls = object.getClass();
        // 这里的ReflectUtil使用的是cn.hutool.core.util.ReflectUtil，会循环查找所有层级父类的字段
        Field[] fields = ReflectUtil.getFields(cls);
        Map<String, Field> fieldMap = new HashMap<>(5);
        //子类字段覆盖父类配置
        for (Field field : fields) {
            String name = field.getName();
            //子类字段覆盖父类配置
            boolean isNoContainField = !fieldMap.containsKey(name);
            if (isNoContainField) {
                fieldMap.put(name, field);
            }
        }
        Collection<Field> fieldList = fieldMap.values();
        Map<String, Object> fieldNameToValue = new HashMap<>(fieldList.size());
        for (Field field : fieldList) {
            //非隐藏字段
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            boolean isShow = apiModelProperty == null || (apiModelProperty != null &&
                    BooleanUtil.isFalse(apiModelProperty.hidden()));
            //非静态字段
            boolean isNoStatic = !ModifierUtil.isStatic(field);
            if (isShow && isNoStatic) {
                Object value = ReflectUtil.getFieldValue(object, field);
                if (value != null && value != object) {
                    Param alias = field.getAnnotation(Param.class);
                    String name = alias != null ? alias.value() : field.getName();
                    fieldNameToValue.put(name, value);
                }
            }
        }
        return fieldNameToValue;
    }
}
