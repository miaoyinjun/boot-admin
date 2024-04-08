package org.jjche.common.serializer.baseenum;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jjche.common.base.IBaseEnum;

import java.io.IOException;

/**
 * <p>
 * 枚举序列化
 * </p>
 *
 * @author miaoyj
 * @since 2022-09-13
 */
public class BaseEnumSerializer extends JsonSerializer<IBaseEnum> {

    @Override
    public void serialize(IBaseEnum IBaseEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(IBaseEnum.getValue());
    }
}
