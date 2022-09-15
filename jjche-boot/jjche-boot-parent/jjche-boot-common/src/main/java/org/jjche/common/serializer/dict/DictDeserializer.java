package org.jjche.common.serializer.dict;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.jjche.common.annotation.Dict;
import org.jjche.common.api.CommonAPI;
import org.jjche.common.dto.DictParam;

import java.io.IOException;
import java.util.Objects;

/**
 * <p>
 * 字典反序列化
 * </p>
 *
 * @author miaoyj
 * @since 2022-09-13
 */
public class DictDeserializer extends StdDeserializer<Object> implements ContextualDeserializer {
    /**
     * 字典注解
     */
    private Dict dict;
    private String type;
    private CommonAPI commonAPI;

    public DictDeserializer() {
        super(Object.class);
    }

    public DictDeserializer(Dict dict) {
        super(Object.class);
        this.dict = dict;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = p.getText();
        if (StrUtil.isBlank(value)) {
            return null;
        }
        if (Objects.nonNull(dict)) {
            type = dict.value();
        }
        // 如果value没有配置到字典，返回null
        if (type != null) {
            DictParam dictParam = commonAPI.getDictByNameValue(type, value);
            return dictParam != null ? value : null;
        }
        return null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext,
                                                BeanProperty beanProperty) throws JsonMappingException {
        if (ObjectUtil.isNull(commonAPI)) {
            commonAPI = SpringUtil.getBean(CommonAPI.class);
        }
        if (Objects.isNull(beanProperty)) {
            return deserializationContext.findContextualValueDeserializer(beanProperty.getType(), beanProperty);
        }
        Dict dict = beanProperty.getAnnotation(Dict.class);
        if (Objects.nonNull(dict)) {
            type = dict.value();
            return this;
        }
        return deserializationContext.findNonContextualValueDeserializer(null);
    }
}