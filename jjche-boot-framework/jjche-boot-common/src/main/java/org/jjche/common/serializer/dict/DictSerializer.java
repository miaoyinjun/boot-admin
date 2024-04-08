package org.jjche.common.serializer.dict;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.jjche.common.annotation.Dict;
import org.jjche.common.api.CommonApi;
import org.jjche.common.dto.DictParam;

import java.io.IOException;
import java.util.Objects;

/**
 * <p>
 * 字典序列化
 * </p>
 *
 * @author miaoyj
 * @since 2022-09-13
 */
public class DictSerializer extends StdSerializer<Object> implements ContextualSerializer {
    /**
     * 字典注解
     */
    private Dict dict;
    private String type;
    private CommonApi commonApi;

    public DictSerializer() {
        super(Object.class);
    }

    public DictSerializer(Dict dict) {
        super(Object.class);
        this.dict = dict;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (Objects.isNull(value)) {
            gen.writeObject(value);
            return;
        }
        if (Objects.nonNull(dict)) {
            type = dict.value();
        }
        // 通过数据字典类型和value获取name
        if (type != null) {
            gen.writeObject(value);
            DictParam dictParam = commonApi.getDictByNameValue(type, String.valueOf(value));
            if (dictParam != null) {
                String filedName = gen.getOutputContext().getCurrentName() + "Label";
                gen.writeFieldName(filedName);
                gen.writeObject(dictParam.getLabel());
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty beanProperty) throws JsonMappingException {
        if (ObjectUtil.isNull(commonApi)) {
            commonApi = SpringUtil.getBean(CommonApi.class);
        }
        if (Objects.isNull(beanProperty)) {
            return prov.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        Dict dict = beanProperty.getAnnotation(Dict.class);
        if (Objects.nonNull(dict)) {
            type = dict.value();
            return this;
        }
        return prov.findNullValueSerializer(null);
    }
}
